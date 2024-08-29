import matplotlib.pyplot as plt
import matplotlib.animation as animation
import numpy as np
import utils

def update(frame, scat, quiv, positions, angles, time_steps, L, cmap):

    if frame == 0:
        return scat, quiv

    if frame % (len(time_steps) // 10) == 0:
        print(f'Progress: {int(frame / len(time_steps) * 100)}%')

    # Update scatter plot
    x_data, y_data = zip(*positions[frame])
    colors = cmap(np.array(angles[frame]) / (2 * np.pi))  # Normalize angles to [0, 1] for colormap

    scat.set_offsets(np.c_[x_data, y_data])
    scat.set_color(colors)

    # Update quiver plot
    u = np.cos(angles[frame]) * (L / 30)
    v = np.sin(angles[frame]) * (L / 30)
    quiv.set_offsets(np.c_[x_data, y_data])
    quiv.set_UVC(u, v)
    quiv.set_color(colors)  # Set the color of the quiver arrows


    return scat, quiv

def create_animation(time_steps, positions, angles, L, output_file='data/animation_particles_only.mp4'):

    fig, (ax) = plt.subplots(figsize=(10, 10))

    x_min, x_max = 0, L
    y_min, y_max = 0, L
    ax.set_xlim(x_min, x_max)
    ax.set_ylim(y_min, y_max)

    # No ticks
    ax.set_xticks([])
    ax.set_yticks([])

    print("Creating animation...")

    scat = ax.scatter([], [], s=10)

    init_pos = positions[0]
    init_angle = angles[0]
    u = np.cos(init_angle) * (L / 30)
    v = np.sin(init_angle) * (L / 30)

    cmap = plt.cm.hsv  # Use HSV colormap

    quiv = ax.quiver(
        [pos[0] for pos in init_pos],
        [pos[1] for pos in init_pos],
        u, v, angles='xy', scale_units='xy', scale=1
    )
    quiv.set_color(cmap(np.array(init_angle) / (2 * np.pi)))

    interval = 1000 / 15


    ani = animation.FuncAnimation(
        fig,
        update,
        frames=len(time_steps),
        fargs=(scat, quiv, positions, angles, time_steps, L, cmap),
        interval=interval,
        blit=True
    )

    ani.save(output_file, writer='ffmpeg')

def plot_positions(positions, L, t, file_path='data/initial_positions.png'):
    _, ax = plt.subplots(figsize=(10, 10))
    ax.set_xlim(0, L)
    ax.set_ylim(0, L)
    ax.scatter([pos[0] for pos in positions[t]], [pos[1] for pos in positions[t]], s=10)
    ax.set_xticks([])
    ax.set_yticks([])

    cmap = plt.cm.hsv
    colors = cmap(np.array(angles[t]) / (2 * np.pi))  # Normalize angles to [0, 1] for colormap

    ax.quiver(
        [pos[0] for pos in positions[t]],
        [pos[1] for pos in positions[t]],
        np.cos(angles[t]) * (L / 30), np.sin(angles[t]) * (L / 30), angles='xy', scale_units='xy', scale=1,
        color=colors
    )



    plt.savefig(file_path)

file_path = 'data/dynamic.txt'
time_steps, positions, angles = utils.parse_dynamic_file(file_path)
L, _, v, noise, rc = utils.parse_static_file('data/static.txt')
create_animation(time_steps, positions, angles, L)
plot_positions(positions, L, 10, 'data/positions_10.png')
plot_positions(positions, L, 500, 'data/positions_500.png')
