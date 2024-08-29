import matplotlib.pyplot as plt
import utils
import numpy as np

def plot_polarity(time_steps, angles, vel, output_file='data/polarity.png'):
    _, ax = plt.subplots()

    polarities = []
    for i in range(len(time_steps)):
        # vx: cos(angle), vy: sin(angle)
        vx, vy = np.cos(angles[i]) * vel, np.sin(angles[i]) * vel

        # Add all vectors and calculate magnitude
        v_sum = np.sum(vx), np.sum(vy)
        mag = np.linalg.norm(v_sum)
        polarity = mag / (len(angles[i]) * vel)
        polarities.append(polarity)


    ax.plot(time_steps, polarities)


    ax.set_xlim(0, time_steps[-1])
    ax.set_ylim(0, 1)
    ax.set_xlabel('Time step')
    ax.set_ylabel('Polarity')

    plt.savefig(output_file)
    
def plot_multiple_polarities(time_steps, multiple_angles, labels, vel, output_file='data/polarities.png'):
    fig, ax = plt.subplots()

    # Ensure the number of labels matches the number of angle sets
    if len(labels) != len(multiple_angles):
        raise ValueError("Number of labels must match the number of angle sets")

    for angles, label in zip(multiple_angles, labels):
        polarities = []
        for i in range(len(time_steps)):
            # vx: cos(angle), vy: sin(angle)
            vx, vy = np.cos(angles[i]) * vel, np.sin(angles[i]) * vel

            # Add all vectors and calculate magnitude
            v_sum = np.sum(vx), np.sum(vy)
            mag = np.linalg.norm(v_sum)
            polarity = mag / (len(angles[i]) * vel)
            polarities.append(polarity)

        ax.plot(time_steps, polarities, label=label)

    ax.set_xlim(0, time_steps[-1])
    ax.set_ylim(0, 1)
    ax.set_xlabel('Time step')
    ax.set_ylabel('Polarity')

    # Shrink current axis by 20%
    box = ax.get_position()
    ax.set_position([box.x0, box.y0, box.width * 0.8, box.height])

    # Put a legend to the right of the current axis
    ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))

    plt.savefig(output_file)
    plt.close(fig)

def plot_means_and_deviations(means, deviations, labels, output_file='data/means_deviations.png'):
    fig, ax = plt.subplots()

    # Ensure the number of labels matches the number of angle sets
    if len(labels) != len(means):
        raise ValueError("Number of labels must match the number of angle sets")

    for mean, deviation, label in zip(means, deviations, labels):
        ax.errorbar(mean, deviation, label=label)

    ax.set_xlim(0, 1)
    ax.set_ylim(0, 1)
    ax.set_xlabel('Mean')
    ax.set_ylabel('Deviation')

    # Shrink current axis by 20%
    box = ax.get_position()
    ax.set_position([box.x0, box.y0, box.width * 0.8, box.height])

    # Put a legend to the right of the current axis
    ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))

    plt.savefig(output_file)
    plt.close(fig)




file_path = 'data/dynamic.txt'
time_steps, positions, angles = utils.parse_dynamic_file(file_path)
L, _, v, noise, rc = utils.parse_static_file('data/static.txt')
plot_polarity(time_steps, angles, v, 'data/polarity.png')
