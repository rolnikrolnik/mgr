rm vectorAdd.cu.o
rm vectorAdd 
/usr/local/cuda-6.0/bin/nvcc -ccbin g++ -I/usr/local/cuda/samples/common/inc  -m64     -o vectorAdd.cu.o -c vectorAdd.cu
/usr/local/cuda-6.0/bin/nvcc -ccbin g++ -m64 -o vectorAdd vectorAdd.cu.o 




