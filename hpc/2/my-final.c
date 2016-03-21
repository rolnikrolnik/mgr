#include<mpi.h>
#include<stdio.h>
#include<math.h>

#define PRECISION 0.000001
#define RANGESIZE 1
#define DATA 0
#define RESULT 1
#define FINISH 2
#define INITIAL_DATASIZE 5
//#define DEBUG

double f(double x) {
    return sin(x)*sin(x)/x;
}

double SimpleIntegration(double a,double b) {
    double i;
    double sum=0;
    for (i=a; i<b; i+=PRECISION)
    sum+=f(i)*PRECISION;
    return sum;
}

int main(int argc, char **argv) 
{
    int myrank, proccount;
    double a=1,b=100;
    double range[2];
    double result=0, resulttemp, sendResult;
    int sent = 0, received = 0;
    int i;

    MPI_Status status;
    // Initialize MPI
    MPI_Init(&argc, &argv);
    // find out my rank
    MPI_Comm_rank(MPI_COMM_WORLD, &myrank);
    // find out the number of processes in MPI_COMM_WORLD
    MPI_Comm_size(MPI_COMM_WORLD, &proccount);

    if (proccount<2) {
        printf("Run with at least 2 processes");
        MPI_Finalize();
        return -1;
    }
    
    if (((b-a)/RANGESIZE)<2*(proccount-1)) {
        printf("More subranges needed");
        MPI_Finalize();
        return -1;
    }

    // now the master will distribute the data and slave processes will perform computations
    if (myrank==0) {

		range[0] = a;
		// first distribute some ranges to all slaves
		for (i = 1; i < proccount; i++) {
			int j;
			for(j = 0; j < INITIAL_DATASIZE; j++)
			{
				range[1] = range[0] + RANGESIZE;
				// send it to process i
				MPI_Send(range, 2, MPI_DOUBLE, i, DATA, MPI_COMM_WORLD);
				sent++;
				range[0] = range[1];
			}
        }
        
        do {
            // distribute remaining subranges to the processes which have completed their parts
            MPI_Recv(&resulttemp,1,MPI_DOUBLE,MPI_ANY_SOURCE,RESULT,MPI_COMM_WORLD,&status);
			result+=resulttemp;
			received++;

            // check the sender and send some more data
            range[1]=range[0]+RANGESIZE;
            if (range[1] > b) range[1] = b;

            MPI_Send(range,2,MPI_DOUBLE,status.MPI_SOURCE,DATA,MPI_COMM_WORLD);
			sent++;
            range[0]=range[1];

        } while (range[1]<b);
		// shut down the slaves

		for (i = 1; i<proccount; i++)
		{
			MPI_Send(NULL, 0, MPI_DOUBLE, i, FINISH, MPI_COMM_WORLD);
		}

        // now receive results from the processes
        //for(i=0;i < (proccount-1);i++) {
		while(received < sent){
            MPI_Recv(&resulttemp,1,MPI_DOUBLE,MPI_ANY_SOURCE,RESULT,MPI_COMM_WORLD,&status);
			received++;
            #ifdef DEBUG
            printf("\nMaster received result %f from process %d",resulttemp,status.MPI_SOURCE);
            fflush(stdout);
            #endif

            result+=resulttemp;
        }

        // now display the result
        printf("\nHi, I am process 0, the result is %f\n",result);

    } else { 
		// slave
		MPI_Request sendRequest = MPI_REQUEST_NULL;
		do
		{
		    MPI_Probe(0,MPI_ANY_TAG,MPI_COMM_WORLD,&status);
		    if (status.MPI_TAG==DATA)
		    {
		        MPI_Recv(range, 2,MPI_DOUBLE,0,DATA,MPI_COMM_WORLD,&status);
		
		        // compute my part
		        resulttemp = SimpleIntegration(range[0],range[1]);
				MPI_Wait(&sendRequest, MPI_STATUS_IGNORE);
				sendResult = resulttemp;
		
		        // send the result back
		        MPI_Isend(&sendResult,1,MPI_DOUBLE,0,RESULT,MPI_COMM_WORLD, &sendRequest);
		    }
		} while (status.MPI_TAG != FINISH);
    }

    // Shut down MPI
    MPI_Finalize();
    return 0;
}
    