package login;

import java.io.*;
import java.util.Scanner;
import java.util.Arrays;



class WavFile{
	private File file;    
	private RandomAccessFile iStream;     
	private FileOutputStream oStream;
	
	private long riff;			// RIFF string
	private long overall_size;		// overall size of file in bytes
	private long wave;			// WAVE string
	private long fmt_chunk_marker;	// fmt string with trailing null char
	private long length_of_fmt;		// length of the format data
	private int format_type;		// format type. 1-PCM, 3- IEEE float, 6 - 8bit A law, 7 - 8bit mu law
	private int channels;			//no.of channels (2 bytes unsigned)
	private long sample_rate;		//sampling rate (blocks per second) (4 bytes unsigned)
	private long byterate;			//SampleRate * NumChannels * BitsPerSample/8	
	private int block_align;		//NumChannels * BitsPerSample/8 (2 bytes unsigned)
	private int bits_per_sample;		//bits per sample, 8- 8bits, 16- 16 bits etc
	private long data_chunk_header;	//DATA string or FLLR string
	private long data_size;			//NumSamples * NumChannels * BitsPerSample/8 - size of the next chunk that will be read
   
    //BUFFERING
    private byte buffer[];
    private int bufferPointer;
    private int bytesRead;
    private long frameCount;

    WavFile(){
    	buffer=new byte[4096];
    }

 private static long getLE(byte[] buffer, int pos,long numBytes)
	{  
		numBytes --;
		pos+= numBytes;

		long val = buffer[pos] & 0xFF;
		for (int b=0 ; b<numBytes ; b++) val = (val << 8 )+ (buffer[--pos] & 0xFF);

		return val;
	}

	/*private static void putLE(long val, byte[] buffer, int pos,int numBytes)
	{    
	
		for (int b=0 ; b<numBytes ; b++)
		{
			buffer[pos] = (byte) (val & 0xFF);
			val >>= 8;
			pos ++;
		}
   }*/

public static void display(WavFile obj){

   System.out.println("RiffChunkID :RIFF->"+obj.riff);
   System.out.println("chunkSize :"+obj.overall_size);
   System.out.println("riffTypeID :WAVE ->"+obj.wave+"\n");
   System.out.println("Subchunk1ID :fmt ->"+obj.fmt_chunk_marker);
   System.out.println("Subchunk1Size :"+obj.length_of_fmt);
   System.out.println("Audio Format :"+obj.format_type);
   System.out.println("No of Channels :"+obj.channels);
   System.out.println("Sample Rate :"+obj.sample_rate);
   System.out.println("Byte rate :"+obj.byterate);
   System.out.println("Block Align :"+obj.block_align);
   System.out.println("Bits per Sample :"+obj.bits_per_sample+"\n");
   System.out.println("Subchunk2ID :data ->"+obj.data_chunk_header);
   System.out.println("Subchunk 2 Size:"+obj.data_size);
   
}

public static void plotter(Complex []dftoutput)
{
  //LineChartExample.func(dftoutput);
  System.out.println("**********************************************************************//");
}


public static void openFile(File file)throws IOException{
   WavFile obj=new WavFile();
   obj.file=file;

   obj.iStream = new RandomAccessFile(file,"r");
   obj.iStream.read(obj.buffer,0,12);

   obj.riff =getLE(obj.buffer,0,4);
   obj.overall_size =getLE(obj.buffer,4,4);
   obj.wave =getLE(obj.buffer,8,4);

 
    obj.iStream.read(obj.buffer,0,8);
    obj.length_of_fmt = getLE(obj.buffer,4,4);
    long chunkSize =getLE(obj.buffer,0,4);
    obj.fmt_chunk_marker=chunkSize;
   
    long numChunkBytes = (chunkSize%2==0) ? chunkSize+1 : chunkSize;
   

    obj.iStream.read(obj.buffer,0,16);
    
    obj.format_type=(int)getLE(obj.buffer,0,2);
    obj.channels=(int)getLE(obj.buffer,2,2);
    obj.sample_rate=getLE(obj.buffer,4,4);
    obj.byterate=getLE(obj.buffer,8,4);
    obj.block_align=(int)getLE(obj.buffer,12,2);
    obj.bits_per_sample=(int)getLE(obj.buffer,14,2);

    obj.iStream.read(obj.buffer,0,8);

    obj.data_chunk_header=getLE(obj.buffer,0,4);
    obj.data_size=getLE(obj.buffer,4,4);


 System.out.println("-------------------DISPLAYING DETAILS--------------------");
   display(obj);
  System.out.println("---------------------------------------------------------");
  
   long num_samples = (8 * obj.data_size) / (obj.channels * obj.bits_per_sample);
   long size_of_each_channel =  obj.bits_per_sample / 8;

  long q=num_samples / (4096/(size_of_each_channel*obj.channels));
  long r=num_samples % (4096/(size_of_each_channel*obj.channels));
  //System.out.println(num_samples+"  "+q+"  "+r);
  int running=0;


  //  q  number of frames
  int processing[][] = new int[q][5];

 for(int t=0;t<q;t++){
  int cond=0;


  if(t<q){
    cond=(int)(4096/(size_of_each_channel*obj.channels));
  }else{
    cond=(int)r;
  } 
  obj.iStream.read(obj.buffer,0,(int)(cond*size_of_each_channel*obj.channels));
  running=0;
  Complex input[]=new Complex[cond];
  //int bottle=0;

  for(int i=0;i<cond;i++){

    //System.out.println("=========================================");
    //System.out.println("SAMPLE NO:"+((t*1024)+i+1));
  
    double avg=0;
    for(int j=0;j<obj.channels;j++){
      long x=0;
      if(size_of_each_channel==4){
       x=obj.buffer[running]|(obj.buffer[running+1]<<8)|(obj.buffer[running+2]<<16)|(obj.buffer[running+3]<<24);
      }else if(size_of_each_channel==2){
       x=obj.buffer[running]|(obj.buffer[running+1]<<8);
      }else if(size_of_each_channel==1){
       x=obj.buffer[running];
      }
      avg+=x;

      //System.out.println("CHANNEL NO "+(j+1)+":"+x);
      running+=size_of_each_channel;
    }
    input[i]=new Complex(avg,0.0);
    
    //System.out.println(y+" "+running);
  }
  Complex dftoutput[]=Complex.fft(input);

  //------------------------------------GETTING FREQUENCIES FROM THE DATA------------------------------------------------

 int bin;
 double frequency[] = new double[dftoutput.length/2 + 1];
 double Amplitude[] = new double[dftoutput.length/2 + 1];

  for(bin=0;bin<=dftoutput.length/2;bin++){
      
       //frequency
       frequency[bin] = (double)bin * obj.sample_rate /(double)dftoutput.length;
       //Amplitude 
       Amplitude[bin] = (dftoutput[bin].re)*(dftoutput[bin].re) + (dftoutput[bin].im)*(dftoutput[bin].im);
       //System.out.println("#  Frequency "+t+ "."+bin+" " +frequency[bin] + " # Amplitude "+t+". "+bin+" "+Amplitude[bin]);
   
  }

  
  Arrays.sort(frequency);
  for(int i=0;i<5;i++){
    processing[t][i] = frequency[n-i-1];
  }

  

// System.out.println();

//----------------------------------FEQUENCIES OBTAINED----------------------------------------------



  //LineChartExample.func(dftoutput);
  //LineChartExample.func(input);
  
  //break;
}


   //HASH VALUE PROCESSING

   for(int i=0;i<frequency.length;i++){
    for(int j=0;j<5;j++){
      cout<<frequency[i][j]<<" ";
    }
    cout<<endl;
   }



 }



/*
public static void main(String args[]){

	System.out.println("\nEnter the name of the file to be opened");
	Scanner in = new Scanner(System.in);
    String filename=in.nextLine();
    try{
    WavFile.openFile(new File (filename));
    }

catch(Exception e){
	System.out.println(e);
	e.printStackTrace();
	}
}*/

}
