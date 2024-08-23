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
    
def plot_multiple_polarities(time_steps, multiple_angles, vel, output_file='data/polarities.png'):
    _, ax = plt.subplots()

    for angles in multiple_angles:
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



file_path = 'data/dynamic.txt'
time_steps, positions, angles = utils.parse_dynamic_file(file_path)
L, _, v, noise, rc = utils.parse_static_file('data/static.txt')
plot_polarity(time_steps, angles, v, 'data/polarity.png')
