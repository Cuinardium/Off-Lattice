import concurrent.futures
import utils
import os
import numpy as np
import graph_polarity


def execute_simulation(N, L, v, noise, rc, t):
    os.system(f'java -jar ../target/off-lattice-1.0-SNAPSHOT-jar-with-dependencies.jar -out data -N {N} -L {L} -v {v} -n {noise} -rc {rc} -t {t}')

# graph polarity for multiple densities, mantain noise constant
def polarities_multiple_densities(
        N,
        Ls,
        qty_to_graph,
        v,
        noise,
        rc,
        t,
        densities,
        output_file
):
    angles_arr = []
    time_steps_arr = []
    for L in Ls:
        execute_simulation(N, L, v, noise, rc, t)
        time_steps, _, angles = utils.parse_dynamic_file('data/dynamic.txt')
        time_steps_arr.append(time_steps)
        angles_arr.append(angles)

    # check if all time steps are the same. if not, fail
    if not all([time_steps == time_steps_arr[0] for time_steps in time_steps_arr]):
        raise ValueError('Time steps are not the same for all densities')

    # create a subset of only {qty_to_graph} densities (equally spaced) and graph that
    angles_subset = []
    densities_subset = []
    step = len(densities) // qty_to_graph
    for i in range(0, len(densities), step):
        angles_subset.append(angles_arr[i])
        densities_subset.append(densities[i])

    graph_polarity.plot_multiple_polarities(time_steps_arr[0], angles_subset, densities_subset, v, output_file)
    return time_steps_arr[0], angles_arr

def polarities_multiple_noises(
        N,
        L,
        qty_to_graph,
        v,
        noises,
        rc,
        t,
        output_file
):
    angles_arr = []
    time_steps_arr = []
    for noise in noises:
        execute_simulation(N, L, v, noise, rc, t)
        time_steps, _, angles = utils.parse_dynamic_file('data/dynamic.txt')
        time_steps_arr.append(time_steps)
        angles_arr.append(angles)

    # check if all time steps are the same. if not, fail
    if not all([time_steps == time_steps_arr[0] for time_steps in time_steps_arr]):
        raise ValueError('Time steps are not the same for all densities')

    # create a subset of only {qty_to_graph} noises (equally spaced) and graph that
    angles_subset = []
    noises_subset = []
    step = len(noises) // qty_to_graph
    for i in range(0, len(noises), step):
        angles_subset.append(angles_arr[i])
        noises_subset.append(noises[i])

    graph_polarity.plot_multiple_polarities(time_steps_arr[0], angles_subset, noises_subset, v, output_file)
    return time_steps_arr[0], angles_arr

def get_stationary_start_time(time_steps, angles, delta, num_to_consider):
    # find the time step where the mean angle stabilizes in each of the simulations
    # if the standard deviation of the last {num_to_consider} angles is minor to delta, then the mean angle has stabilized
    start_times = []
    for i in range(len(angles)):
        for j in range(num_to_consider, len(angles[i])):
            if np.std(angles[i][j - num_to_consider:j]) < delta:
                start_times.append(time_steps[j])
                break
    return start_times

def get_means_and_deviations_from_stationary_time(angles, start_times):
    means = []
    deviations = []
    for i in range(len(angles)):
        mean = np.mean(angles[i][start_times[i]:])
        deviation = np.std(angles[i][start_times[i]:])
        means.append(mean)
        deviations.append(deviation)
    return means, deviations

if __name__ == '__main__':
    r = 1
    t = 1000
    N = 300
    v = 0.03

    noises = list(np.linspace(0.1, 2.0, 10))
    Ls = list(np.linspace(1, 10, 10).astype(int))

    # density = N / L^2
    densities = [N / L ** 2 for L in Ls]

    delta = 0.5
    num_to_consider = t // 10

    with concurrent.futures.ThreadPoolExecutor() as executor:
        future_densities = executor.submit(polarities_multiple_densities, N, Ls, 3, v, 0.5, r, t, densities, 'data/polarities_mult_densities.png')
        future_noises = executor.submit(polarities_multiple_noises, N, 10, 3, v, noises, r, t, 'data/polarities_mult_noises.png')

        densities_time_steps, densities_angles = future_densities.result()
        noises_time_steps, noises_angles = future_noises.result()

        future_densities_start_times = executor.submit(get_stationary_start_time, densities_time_steps, densities_angles, delta, num_to_consider)
        future_noises_start_times = executor.submit(get_stationary_start_time, noises_time_steps, noises_angles, delta, num_to_consider)

        densities_start_times = future_densities_start_times.result()
        noises_start_times = future_noises_start_times.result()

        future_densities_means = executor.submit(get_means_and_deviations_from_stationary_time, densities_angles, densities_start_times)
        future_noises_means = executor.submit(get_means_and_deviations_from_stationary_time, noises_angles, noises_start_times)

        densities_means, densities_deviations = future_densities_means.result()
        noises_means, noises_deviations = future_noises_means.result()

        future_plot_densities = executor.submit(graph_polarity.plot_means_and_deviations, densities_means, densities_deviations, densities, 'data/means_deviations_densities.png')
        future_plot_noises = executor.submit(graph_polarity.plot_means_and_deviations, noises_means, noises_deviations, noises, 'data/means_deviations_noises.png')

        future_plot_densities.result()
        future_plot_noises.result()
