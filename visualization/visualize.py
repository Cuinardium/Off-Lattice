import matplotlib.pyplot as plt
import matplotlib.animation as animation
import numpy as np

def parse_dynamic_file(file_path):
    time_steps = []
    positions = []
    angles = []
    with open(file_path, 'r') as file:
        lines = file.readlines()

    current_time = None
    for line in lines:
        line = line.strip()
        if line.isdigit():
            current_time = int(line)
            time_steps.append(current_time)
            positions.append([])
            angles.append([])
        else:
            parts = line.split()
            x, y, angle = float(parts[0]), float(parts[1]), float(parts[2])
            positions[-1].append((x, y))
            angles[-1].append(angle)

    return time_steps, positions, angles

def parse_static_file(file_path):
    with open(file_path, 'r') as file:
        lines = file.readlines()

    L = int(lines[0].strip())
    seed = int(lines[1].strip())
    v = float(lines[2].strip())
    noise = float(lines[3].strip())
    rc = float(lines[4].strip())
    return L, seed, v, noise, rc

previous_polarities = []
def update(frame, scat, quiv, line, positions, angles, time_steps, vel, L, cmap):
    if len(previous_polarities) > frame:
        return scat, quiv, line

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

    # Update mean angle plot
    # vx: cos(angle), vy: sin(angle)
    vx, vy = np.cos(angles[frame]) * vel, np.sin(angles[frame]) * vel

    # Add all vectors and calculate magnitude
    v_sum = np.sum(vx), np.sum(vy)
    mag = np.linalg.norm(v_sum)
    polarity = mag / (len(angles[frame]) * vel)
    previous_polarities.append(polarity)

    if len(previous_polarities) == frame + 1:
        line.set_data(time_steps[:frame+1], previous_polarities)

    return scat, quiv, line

def create_animation(time_steps, positions, angles, L, vel, noise, rc, output_file='data/animation.mp4'):
    print(f'L: {L}, v: {vel}, noise: {noise}, rc: {rc}')

    fig, (ax, ax2) = plt.subplots(2, 1, figsize=(10, 10))

    x_min, x_max = 0, L
    y_min, y_max = 0, L
    ax.set_xlim(x_min, x_max)
    ax.set_ylim(y_min, y_max)

    print("Creating animation...")

    scat = ax.scatter([], [], s=10)

    init_pos = positions[0]
    init_angle = angles[0]
    u = np.cos(init_angle)
    v = np.sin(init_angle)

    quiv = ax.quiver(
        [pos[0] for pos in init_pos],
        [pos[1] for pos in init_pos],
        u, v, angles='xy', scale_units='xy', scale=1
    )

    ax2.set_xlim(min(time_steps), max(time_steps))
    ax2.set_ylim(0, 1) # Assuming angles are in radians and mean should be within this range

    line, = ax2.plot([], [], 'b-', lw=1)

    interval = 1000 / 15

    cmap = plt.cm.hsv  # Use HSV colormap

    ani = animation.FuncAnimation(
        fig,
        update,
        frames=len(time_steps),
        fargs=(scat, quiv, line, positions, angles, time_steps, vel, L, cmap),
        interval=interval,
        blit=True
    )

    ani.save(output_file, writer='ffmpeg')

file_path = 'data/dynamic.txt'
time_steps, positions, angles = parse_dynamic_file(file_path)
L, _, v, noise, rc = parse_static_file('data/static.txt')
create_animation(time_steps, positions, angles, L, v, noise, rc)