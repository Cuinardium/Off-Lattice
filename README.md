# Off Lattice
This project is a Command Line Interface (CLI) tool that simulates an Off-Lattice model based on the paper [Novel Type of Phase Transition in a System of Self-Driven Particles.](https://labs.engineering.asu.edu/acs/wp-content/uploads/sites/33/2016/09/Novel-Type-of-Phase-Transition-in-a-System-of-Self-Driven-Particles-1995.pdf) The simulation involves particles moving within a square area, with their velocities influenced by their neighbors.

![animation](https://github.com/user-attachments/assets/8ce1f207-5b9f-4e2a-b373-a9c287688c8b)


## Requirements
- Java 18 or higher
- Maven

## Building the Project
To build the project, navigate to the project directory and use Maven:

```bash
mvn clean package
```

The resulting JAR file will be located in the target directory.

## Usage
To execute the program, use the following command:

```bash
java -jar off-lattice-1.0-SNAPSHOT-jar-with-dependencies.jar [options]
```

### Options
Short|	Long|	Argument|	Description
--- | --- | --- | ---
-h	|--help	|(none)|	Show help information and usage instructions.
-out|	--output|	\<directory\>|	Output directory where results will be stored.
-s	|--seed|	\<long\>	|Seed for the random number generator.
-N	|--number|	\<int\>	|Number of particles to generate.
-L	|--length	|\<long\>	|Length of the area to be analyzed.
-v	|--velocity|	\<double\>	|Velocity of the particles.
-n	|--noise|	\<double\>	|Noise level in the velocity of the particles.
-rc	|--interaction-radius	|\<double\>	|Interaction radius within which particles influence each other's direction.
-t	|--time|	\<int\>	|Number of time steps to simulate.

## Output File Format
The output files will be saved in the specified output directory and follow a specific format:

### `dynamic.txt`
This file contains the position and direction of each particle at each time step.

-  Each block of data begins with the time step (starting from 0).
-  Following lines in the block contain the X coordinate, Y coordinate, and direction of each particle.

Example `dynamic.txt`:
```
0
10.0 20.0 1.5708
50.0 50.0 0.7854
80.0 10.0 3.1416
1
10.1 20.2 1.5710
50.3 50.1 0.7852
80.4 10.1 3.1417
```

- Time step 0:
  - Particle 1 at (10.0, 20.0) with direction 1.5708 radians.
  - Particle 2 at (50.0, 50.0) with direction 0.7854 radians.
  - Particle 3 at (80.0, 10.0) with direction 3.1416 radians.
- Time step 1:
  - Particle 1 moved to (10.1, 20.2) with direction 1.5710 radians.
  - And so on.

### `static.txt`
This file contains the static parameters of the simulation:

- Line 1: Length of the area (L)
- Line 2: Seed for the random number generator
- Line 3: Velocity of the particles
- Line 4: Noise level in the velocity
- Line 5: Interaction radius (rc)

Example `static.txt`:
```
100
42
1.0
0.1
2.5
```

- Area length: 100
- Seed: 42
- Particle velocity: 1.0
- Noise: 0.1
- Interaction radius: 2.5
