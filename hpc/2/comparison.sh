echo "Running example:"
time mpirun -np 16 -machinefile machinefile example

echo "Running mine:"
time mpirun -np 16 -machinefile machinefile 2
