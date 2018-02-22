import java.io.*;
import java.util.Scanner;

//always use .equals to compare strings in java instead of ==
//SYMTAB - To access, start with 1 as symptr and not 0
public class loader{
    public static void main(String[] args){
        //try{
            File file = new File("ass31.asm");
			File file2 = new File("ass32.asm");
			
            long a,b,mem,blck,c;
            a=file.length();b=file2.length();
            c=a+b;
            Scanner sc=new Scanner(System.in);  
            System.out.println("Enter the memory size:");
            mem=sc.nextLong();
            System.out.println("Enter the block size:");
            blck=sc.nextLong();

            if(c<mem){
                System.out.print("The programs fit in the memory and consumes ");
                if(c<blck){
                    System.out.println("1 block of memory");
                }
                else{
                    int nb;
                    nb = (int)Math.ceil((double)c/(double)blck);
                    System.out.println(nb+" blocks of memory");
                }
            }
            else{
                System.out.println("The programs do not fit in the memory");
            }
            
            System.out.println("a-"+a+" b-"+b);
        //}
        //catch (IOException e) {
		//	e.printStackTrace();
		//}

    }
}