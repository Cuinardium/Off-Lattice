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
        output_file
):
    # density = N / L^2
    densities = [N / L**2 for L in Ls]

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

    # create a subset of only 3 densities (equally spaced) and graph that
    angles_subset = []
    densities_subset = []
    step = len(densities) // qty_to_graph
    for i in range(0, len(densities), step):
        angles_subset.append(angles_arr[i])
        densities_subset.append(densities[i])

    graph_polarity.plot_multiple_polarities(time_steps_arr[0], angles_subset, densities_subset, v, output_file)

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

    # create a subset of only 3 densities (equally spaced) and graph that
    angles_subset = []
    noises_subset = []
    step = len(noises) // qty_to_graph
    for i in range(0, len(noises), step):
        angles_subset.append(angles_arr[i])
        noises_subset.append(noises[i])

    graph_polarity.plot_multiple_polarities(time_steps_arr[0], angles_subset, noises_subset, v, output_file)


if __name__ == '__main__':
    r = 1
    t = 2000
    N = 300
    v = 0.03

    noises = list(np.linspace(0.1, 2.0, 10))
    Ls = list(np.linspace(10, 100, 10).astype(int))

    polarities_multiple_densities(N, Ls, 3, v, 0.5, r, t, 'data/polarities_mult_densities.png')
    polarities_multiple_noises(N, 10, 3, v, noises, r, t, 'data/polarities_mult_noises.png')

