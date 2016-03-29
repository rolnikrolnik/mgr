#include<mpi.h>
#include<stdio.h>
#include<math.h>
#include<queue>

using namespace std;

#define PRECISION 0.000001
#define RANGESIZE 1
#define DATA 0
#define RESULT 1
#define FINISH 2
#define INITIAL_PACKETS 4

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
    queue<double> receivingQueue;
    queue<double> sendingQueue;
    int myrank, proccount;
    double a=1,b=100;
    double range[2];
    double result=0;
    double resulttemp = 0;
    int sent = 0, received = 0, inTransit = 0;
    int i, sendResult;

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
	  for(j = 0; j < INITIAL_PACKETS; j++)
	  {
	    range[1] = range[0] + RANGESIZE;
	    MPI_Send(range, 2, MPI_DOUBLE, i, DATA, MPI_COMM_WORLD);
	    inTransit++;
	    range[0] = range[1];
	  }
    }

    do {
        // distribute remaining subranges to the processes which have completed their parts
        MPI_Recv(&resulttemp,1,MPI_DOUBLE,MPI_ANY_SOURCE,RESULT,MPI_COMM_WORLD,&status);
	    result+=resulttemp;
        inTransit--;

        // check the sender and send some more data
        range[1]=range[0] + RANGESIZE;
        if (range[1] > b) range[1] = b;

        MPI_Send(range,2,MPI_DOUBLE,status.MPI_SOURCE,DATA,MPI_COMM_WORLD);
        inTransit++;
    
        range[0]=range[1];
    } while (range[1]<b);
	
    for (i = 1; i<proccount; i++)
	{
        MPI_Send(NULL, 0,MPI_DOUBLE, i, FINISH, MPI_COMM_WORLD);
	}

	while(inTransit)
    {
        MPI_Recv(&resulttemp,1,MPI_DOUBLE,MPI_ANY_SOURCE,RESULT,MPI_COMM_WORLD,&status);
	    result+=resulttemp;
	    inTransit--;
    }

    printf("\nHi, I am process 0, the result is %f\n",result);

    } 
    else 
    { 
	// slave
    MPI_Status probeStatus;
	MPI_Request sendRequest, receiveRequest;
	sendRequest = receiveRequest = MPI_REQUEST_NULL;

    int messageWaiting = false, shouldFinish = false, sendingDone = false;

    for (int i = 0; i < INITIAL_PACKETS; ++i)
    {
        MPI_Recv(&range, 2, MPI_DOUBLE, 0, DATA,MPI_COMM_WORLD, &status);
        receivingQueue.push(range[0]);
        receivingQueue.push(range[1]);
    }	
	
	while (!receivingQueue.empty() || !shouldFinish)
	{
        MPI_Iprobe(0, MPI_ANY_TAG, MPI_COMM_WORLD, &messageWaiting, &probeStatus);
        if(messageWaiting)
        {
            if(probeStatus.MPI_TAG == DATA){
                MPI_Recv(&range, 2, MPI_DOUBLE, 0, DATA,MPI_COMM_WORLD, &status);
                receivingQueue.push(range[0]);
                receivingQueue.push(range[1]);
            }

            // set flag finishing 
            if(probeStatus.MPI_TAG == FINISH){
                shouldFinish = true;
                // thile there are results to send - send them
                while(!sendingQueue.empty()){
                    MPI_Test(&sendRequest, &sendingDone, MPI_STATUS_IGNORE);
                    if(sendingDone){
                        resulttemp = sendingQueue.front();
                        sendingQueue.pop();
                        MPI_Isend(&resulttemp,1,MPI_DOUBLE,0,RESULT,MPI_COMM_WORLD, &sendRequest);
                    }
                }
            }
        }
        
        // if fifo not empty, get value, compute, push to send and send if possible
        if(!receivingQueue.empty())
        {
            range[0] = receivingQueue.front();
            receivingQueue.pop();
            range[1] = receivingQueue.front();
            receivingQueue.pop();

            resulttemp = SimpleIntegration(range[0], range[1]);
            sendingQueue.push(resulttemp);

            // check if previous send is done
            MPI_Test(&sendRequest, &sendingDone, MPI_STATUS_IGNORE);
            if(sendingDone)
            {
                resulttemp = sendingQueue.front();
                sendingQueue.pop();
                MPI_Isend(&resulttemp,1,MPI_DOUBLE,0,RESULT,MPI_COMM_WORLD, &sendRequest);
            }
        }
	}
    }

    // Shut down MPI
    MPI_Finalize();
    return 0;
}

// #define QUEUE_ELEMENTS 100
// #define QUEUE_SIZE (QUEUE_ELEMENTS + 1)
// int Queue[QUEUE_SIZE];
// int QueueIn, QueueOut;

// void QueueInit(void)
// {
//     QueueIn = QueueOut = 0;
// }

// int QueuePut(int item)
// {
//     if(QueueIn == (( QueueOut - 1 + QUEUE_SIZE) % QUEUE_SIZE))
//     {
//         return -1; /* Queue Full*/
//     }

//     Queue[QueueIn] = item;

//     QueueIn = (QueueIn + 1) % QUEUE_SIZE;

//     return 0; // No errors
// }

// bool IsQueueEmpty(){
//     if(QueueIn == QueueOut){
//         return true;
//     }
//     return false;
// }

// bool QueueGet(int *old)
// {
//     if(IsQueueEmpty())
//     {
//         return false; /* Queue Empty - nothing to get*/
//     }

//     *old = Queue[QueueOut];

//     QueueOut = (QueueOut + 1) % QUEUE_SIZE;

//     return true; // No errors
// }
