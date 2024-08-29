import concurrent.futures
import sys
import shutil
import utils
import os
import json
import numpy as np
import plots

global concurrent_count


def execute_simulation(N, L, v, noise, rc, t, root_dir="data"):

    # Create a unique directory based on the parameters
    unique_dir = f"{root_dir}/N_{N}_L_{L}_v_{v}_noise_{noise}_rc_{rc}_t_{t}"

    os.makedirs(unique_dir, exist_ok=True)

    global concurrent_count

    print(f"Running simulation for N={N}, L={L}, noise={noise}")
    print(f"Left to run: {concurrent_count}")

    concurrent_count -= 1

    os.system(
        f"java -jar ../target/off-lattice-1.0-SNAPSHOT-jar-with-dependencies.jar -out {unique_dir} -N {N} -L {L} -v {v} -n {noise} -rc {rc} -t {t}"
    )

    print(f"Simulation for N={N}, L={L}, noise={noise} completed")

    return unique_dir


def execute_simulations(N_list, L_list, noise_list, v, rc, t, root_dir="data"):
    # Create a thread pool for parallel execution
    with concurrent.futures.ThreadPoolExecutor(max_workers=10) as executor:
        # Store futures for all simulation tasks
        futures = []

        global concurrent_count
        concurrent_count = len(N_list) * len(L_list) * len(noise_list)

        for N in N_list:
            for L in L_list:
                for noise in noise_list:
                    # Submit the simulation task to the executor
                    futures.append(
                        executor.submit(
                            execute_simulation,
                            N,
                            L,
                            v,
                            noise,
                            rc,
                            t,
                            root_dir + "/simulations",
                        )
                    )

        results = []

        # Wait for all tasks to complete and parse the files
        for future in concurrent.futures.as_completed(futures):
            try:
                unique_dir = (
                    future.result()
                )  # Get the directory of the completed simulation

                # Parse the static and dynamic files from the simulation
                static_file = os.path.join(unique_dir, "static.txt")
                dynamic_file = os.path.join(unique_dir, "dynamic.txt")

                # Parse static and dynamic files
                L, _, v, noise, rc = utils.parse_static_file(static_file)
                _, _, angles = utils.parse_dynamic_file(dynamic_file)
                N = len(angles[0])

                polarities = utils.calculate_polarities(angles, v)

                # Append the parsed data to results
                results.append(
                    {
                        "parameters": {
                            "L": L,
                            "N": N,
                            "noise": noise,
                            "v": v,
                            "rc": rc,
                            "t": t,
                        },
                        "polarities": polarities,
                    }
                )

            except Exception as e:
                print(f"An error occurred during simulation or parsing: {e}")

        # Delete root_dir/simulations
        shutil.rmtree(root_dir + "/simulations", ignore_errors=True)

        return results  # Return the parsed results


def plot_results(results, output_dir="data"):

    selected_l = [2, 4, 5, 8, 10]
    selected_n = 400

    # Map of (L,N) to (means, deviations, noises)
    density_to_data = {}

    for result in results:

        parameters = result["parameters"]
        polarities = result["polarities"]

        if parameters["L"] in selected_l and parameters["N"] == selected_n:
            density = parameters["N"] / parameters["L"] ** 2

            l_n = (parameters["L"], parameters["N"])

            if l_n not in density_to_data:
                density_to_data[l_n] = ([], [], [])

            # Only average last 30% of polarities
            last_30_percent = int(len(polarities) * 0.3)
            polarities = polarities[-last_30_percent:]

            # Calculate mean and deviation
            polarity_means = np.mean(polarities)
            polarity_deviations = np.std(polarities)

            density_to_data[l_n][0].append(polarity_means)
            density_to_data[l_n][1].append(polarity_deviations)
            density_to_data[l_n][2].append(parameters["noise"])

    # Plot polarities versus noise
    plots.plot_polarity_versus_noise(
        density_to_data,
        output_dir=output_dir,
    )
    plots.plot_multiple_polarities_versus_noise(
        density_to_data, output_dir=output_dir
    )

    # Map of (noise) to (means, deviations, densities)
    selected_noises = [0.1, 1, 2, 3, 4, 5]
    noise_to_data = {}
    for result in results:

        parameters = result["parameters"]
        polarities = result["polarities"]

        if parameters["noise"] in selected_noises:
            noise = parameters["noise"]
            density = parameters["N"] / (parameters["L"] ** 2)

            if density > 20:
                continue

            if noise not in noise_to_data:
                noise_to_data[noise] = ([], [], [])

            # Only average last 30% of polarities
            last_30_percent = int(len(polarities) * 0.3)
            polarities = polarities[-last_30_percent:]

            # Calculate mean and deviation
            polarity_means = np.mean(polarities)
            polarity_deviations = np.std(polarities)

            noise_to_data[noise][0].append(polarity_means)
            noise_to_data[noise][1].append(polarity_deviations)
            noise_to_data[noise][2].append(density)

    # Plot polarities versus density
    plots.plot_polarity_versus_density(noise_to_data, output_dir)
    plots.plot_multiple_polarities_versus_density(noise_to_data, output_dir)

    # Plot polarities versus time

    delta = "\u03B4"
    eta = "\u03B7"

    # Multiple noises

    L = 10
    N = 400
    text = f"L={L}\nN={N}\n{delta}={N/(L**2)}"

    noises = [1, 2, 3]
    noises_labels = [f"{eta}={noise}" for noise in noises]
    polarities = []
    time_steps = []

    for noise in noises:
        for result in results:
            parameters = result["parameters"]
            if (
                parameters["L"] == L
                and parameters["N"] == N
                and parameters["noise"] == noise
            ):
                polarities.append(result["polarities"])
                time_steps = list(range(len(result["polarities"])))

    plots.plot_multiple_polarities_versus_time(
        time_steps,
        polarities,
        noises_labels,
        text,
        output_file=f"{output_dir}/polarities_vs_time_noise.png",
    )

    # Multiple densities
    noise = 2
    N = 400

    text = f"{eta}={noise}\nN={N}"

    selected_L = [10, 8, 6]
    densities = [N / (L**2) for L in selected_L]
    density_labels = [f"{delta}={density:.2f}" for density in densities]

    polarities = []
    time_steps = []

    for L in selected_L:
        for result in results:
            parameters = result["parameters"]
            if (
                parameters["L"] == L
                and parameters["N"] == N
                and parameters["noise"] == noise
            ):
                polarities.append(result["polarities"])
                time_steps = list(range(len(result["polarities"])))

    plots.plot_multiple_polarities_versus_time(
        time_steps,
        polarities,
        density_labels,
        text,
        output_file=f"{output_dir}/polarities_vs_time_density.png",
    )


if __name__ == "__main__":

    # If arg is generate, generate data
    # If arg is plot, plot data

    if len(sys.argv) < 2:
        print("Usage: python system_behaviour.py [generate|plot]")
        exit(1)

    if sys.argv[1] == "generate":
        r = 1
        t = 1000
        v = 0.03

        noises = [0.1, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5]
        Ls = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        Ns = [40, 100, 400]

        print("Running simulation for multiple densities and noises")
        print("Noises:", noises)
        print("Ls:", Ls)
        print("Ns:", Ns)

        # density = N / L^2
        densities = [N / L**2 for N in Ns for L in Ls]

        results = execute_simulations(Ns, Ls, noises, v, r, t, root_dir="data")

        print("Dumping results")

        # Save as json
        with open("data/results.json", "w") as json_file:
            json.dump(results, json_file, indent=4)

    elif sys.argv[1] == "plot":
        # Read results from json_file
        with open("data/results.json", "r") as json_file:
            results = json.load(json_file)

            plot_results(results, output_dir="data")

    else:
        print("Usage: python system_behaviour.py [generate|plot]")
