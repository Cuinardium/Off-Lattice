import matplotlib.pyplot as plt
import utils
import numpy as np


def plot_polarityversus_time(time_steps, angles, vel, output_file="data/polarity.png"):
    _, ax = plt.subplots()
    va = "v\u2090"

    polarities = utils.calculate_polarities(angles, vel)

    ax.plot(time_steps, polarities)

    ax.set_xlim(0, time_steps[-1])
    ax.set_ylim(0, 1)
    ax.set_xlabel("Time (t)")
    ax.set_ylabel(f"Polarity ({va})")

    plt.savefig(output_file)


def plot_multiple_polarities_versus_time(
    time_steps, multiple_polarities, labels, text, output_file="data/polarity.png"
):
    fig, ax = plt.subplots()
    va = "v\u2090"

    # Ensure the number of labels matches the number of angle sets
    if len(labels) != len(multiple_polarities):
        raise ValueError("Number of labels must match the number of angle sets")

    for polarities, label in zip(multiple_polarities, labels):

        ax.plot(time_steps, polarities, label=label)

    ax.set_xlim(0, time_steps[-1])
    ax.set_ylim(0, 1)
    ax.set_xlabel("Time (t)")
    ax.set_ylabel(f"Polarity ({va})")

    # Shrink current axis by 20%
    box = ax.get_position()
    ax.set_position([box.x0, box.y0, box.width * 0.8, box.height])

    # Put a legend to the right of the current axis
    ax.legend(loc="center left", bbox_to_anchor=(1, 0.5))

    # Add text above the legend box (surronded by a box)
    ax.text(
        1.05,
        0.8,
        text,
        transform=ax.transAxes,
        fontsize=12,
        verticalalignment="top",
        bbox=dict(facecolor="none", edgecolor="grey", boxstyle="round,pad=0.1"),
    )

    plt.savefig(output_file)
    plt.close(fig)


# density_to_data map [(L, N): (means, deviations, noises)]
def plot_polarity_versus_noise(density_to_data, output_dir="data"):

    for (L, N), (means, deviations, noises) in density_to_data.items():

        fig, ax = plt.subplots()

        density = N / (L**2)
        density_label = f"L={L}\nN={N}\nδ={density:.2f}"

        ax.errorbar(
            noises,
            means,
            yerr=deviations,
            label=density_label,
            fmt="o",
            ecolor="black",
            capsize=5,
            elinewidth=1,
        )

        ax.set_ylim(0, 1.1)

        # ticks [0, 0.2, 0.4, 0.6, 0.8, 1.0]
        ax.set_yticks(np.arange(0, 1.2, 0.2))

        # eta symbol
        eta = "\u03B7"
        ax.set_xlabel(f"Noise ({eta})")

        # va
        va = "v\u2090"
        ax.set_ylabel(f"Polarity ({va})")

        # Shrink current axis by 20%
        box = ax.get_position()
        ax.set_position([box.x0, box.y0, box.width * 0.8, box.height])

        # Put a legend to the right of the current axis
        ax.legend(loc="center left", bbox_to_anchor=(1, 0.5))

        file_name = f"{output_dir}/polarity_versus_noise_{L}_{N}.png"

        plt.savefig(file_name)
        plt.close(fig)


# One plot with multple lines, each line is a different density
def plot_multiple_polarities_versus_noise(density_to_data, output_dir="data"):
    fig, ax = plt.subplots()

    for (L, N), (means, deviations, noises) in density_to_data.items():

        density = N / (L**2)
        density_label = f"L={L}\nN={N}\nδ={density:.2f}"

        ax.errorbar(
            noises,
            means,
            yerr=deviations,
            label=density_label,
            fmt="o",
            capsize=5,
            elinewidth=1,
        )

    ax.set_ylim(0, 1.1)

    # ticks [0, 0.2, 0.4, 0.6, 0.8, 1.0]
    ax.set_yticks(np.arange(0, 1.2, 0.2))

    # eta symbol
    eta = "\u03B7"
    ax.set_xlabel(f"Noise ({eta})")

    # va
    va = "v\u2090"
    ax.set_ylabel(f"Polarity ({va})")

    # Shrink current axis by 20%
    box = ax.get_position()
    ax.set_position([box.x0, box.y0, box.width * 0.8, box.height])

    # Put a legend to the right of the current axis
    ax.legend(loc="center left", bbox_to_anchor=(1, 0.5))

    file_name = f"{output_dir}/polarity_versus_noise.png"

    plt.savefig(file_name)
    plt.close(fig)


def plot_polarity_versus_density(noise_to_data, output_dir="data"):
    eta = "\u03B7"
    delta = "\u03B4"

    for (noise), (means, deviations, densities) in noise_to_data.items():

        fig, ax = plt.subplots()

        ax.errorbar(
            densities,
            means,
            yerr=deviations,
            label=f"{eta}={noise}",
            fmt="o",
            ecolor="black",
            capsize=2,
            elinewidth=1,
        )

        ax.set_ylim(0, 1.1)

        # ticks [0, 0.2, 0.4, 0.6, 0.8, 1.0]
        ax.set_yticks(np.arange(0, 1.2, 0.2))

        ax.set_xlabel(f"Density ({delta})")

        # va
        va = "v\u2090"
        ax.set_ylabel(f"Polarity ({va})")

        # Shrink current axis by 20%
        box = ax.get_position()
        ax.set_position([box.x0, box.y0, box.width * 0.8, box.height])

        # Put a legend to the right of the current axis
        ax.legend(loc="center left", bbox_to_anchor=(1, 0.5))

        file_name = f"{output_dir}/polarity_versus_density_{noise}.png"

        plt.savefig(file_name)
        plt.close(fig)


def plot_multiple_polarities_versus_density(noise_to_data, output_dir="data"):
    fig, ax = plt.subplots()
    eta = "\u03B7"
    delta = "\u03B4"

    for (noise), (means, deviations, densities) in noise_to_data.items():

        ax.errorbar(
            densities,
            means,
            yerr=deviations,
            label=f"{eta}={noise}",
            fmt="o",
            elinewidth=1,
        )

    ax.set_ylim(0, 1.1)

    # ticks [0, 0.2, 0.4, 0.6, 0.8, 1.0]
    ax.set_yticks(np.arange(0, 1.2, 0.2))

    ax.set_xlabel(f"Density ({delta})")

    # va
    va = "v\u2090"
    ax.set_ylabel(f"Polarity ({va})")

    # Shrink current axis by 20%
    box = ax.get_position()
    ax.set_position([box.x0, box.y0, box.width * 0.8, box.height])

    # Put a legend to the right of the current axis
    ax.legend(loc="center left", bbox_to_anchor=(1, 0.5))

    file_name = f"{output_dir}/polarity_versus_density.png"

    plt.savefig(file_name)
    plt.close(fig)
