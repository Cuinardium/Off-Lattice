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



def calculate_polarities(angles, vel):

    polarities = []
    for angle_list in angles:
        # vx: cos(angle), vy: sin(angle)
        vx, vy = np.cos(angle_list) * vel, np.sin(angle_list) * vel

        # Add all vectors and calculate magnitude
        v_sum = np.sum(vx), np.sum(vy)
        mag = np.linalg.norm(v_sum)
        polarity = mag / (len(angle_list) * vel)
        polarities.append(polarity)


    return polarities
