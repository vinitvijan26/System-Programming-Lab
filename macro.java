//package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class macro {

	public static void main(String[] args) {
		try {
			File file = new File("ass1.asm");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			//StringBuffer stringBuffer = new StringBuffer();
            String line;
            
            int flag=0,k=0,k1=0,l=0,j=0,m=0,i1=0,i2=0,s1=0,a,l1=0,l2=0,l3=0,m1=0,j1=0,j2=0;      //k1 - acts as macro no. in FT (layer)
                //s1 - line no. of MDT, a - temporary for string conversions
            String[][] MNT = new String[10][5];
            String[][][] FT = new String[5][2][5];      //formal vs positional table
            String[] MDT = new String[30];
			while ((line = bufferedReader.readLine()) != null) {
				//stringBuffer.append(line);
            	//stringBuffer.append("\n");
                String[] w = line.split("\\s");         //splits the string based on whitespace  
                l=w.length;
                //System.out.println(w[0]);
                if(flag==0){
                    if(w[0].equals("macro")){
                        flag=1;
                        if(l==2){
                            MNT[j][0]=w[1];     //macro name
                            a=0;
                            MNT[j][1]=Integer.toString(a);        //no parameters
                            k++;
                            MNT[j][2]=Integer.toString(k);        //starting address(line) for MDT file
                                                //im sending this k after incrementing, which means for the next line

                            for(i1=1;i1<=1;i1++){           //does as many times as the no. of parameters
                                FT[i1-1][0][k1]="No parameters";
                                FT[i1-1][1][k1]="";
                            }
                            k1++;   
                        }
                        else{
                            MNT[j][0]=w[1];     //macro name
                            //MNT[j][1]=
                            String[] par = w[2].split(",");
                            m = par.length;
                            MNT[j][1]=Integer.toString(m);        //no. of parameters
                            k++;
                            MNT[j][2]=Integer.toString(k);
                            
                            //write the contents of par in FT[][][]
                            //i1=0;i2=0;      //i1 - count for #
                            for(i1=1;i1<=m;i1++){           //does as many times as the no. of parameters
                                FT[i1-1][0][k1]=par[i1-1];
                                String temp = Integer.toString(i1);
                                FT[i1-1][1][k1]="#";                         //TODO: '# + i1'
                                FT[i1-1][1][k1]+=temp;
                            }
                            k1++;       //filled FT till here
                        }
                    }
                    else if(w[0].equals(";")){
                        //ignore the line
                    }
                }
                else if(flag==1){       //code after macro is visited
                    if(w[0].equals(";")){
                        //ignore the line
                    }
                    else if(!(w[0].equals("mend"))){
                        String[] par1 = line.split("\\s");
                        m1 = par1.length;
                        for(i1=0;i1<m1;i1++){        //needs optimization, doing all this for #1, #2 in MDT
                            j2 = Integer.valueOf(MNT[j][1]);
                            for(j1=1;j1<=j2;j1++){
                                if(par1[i1].equals(FT[j1-1][0][k1-1])){
                                    par1[i1]=FT[j1-1][1][k1-1];
                                }
                            }
                        }
                        MDT[s1]="";
                        for(i1=0;i1<m1-1;i1++){
                            MDT[s1]+=par1[i1];
                            MDT[s1]+=" ";
                        }
                        MDT[s1]+=par1[i1];
                        //MDT[s1]+=k;
                        //MDT[s1]+=" ";
                        //MDT[s1]+=line;
                        s1++;
                        k++;
                    }
                    else{
                        MDT[s1]="";
                        //MDT[s1]+=k;
                        //MDT[s1]+=" ";
                        MDT[s1]+=line;
                        s1++;
                        //k++;
                        //no k++ here
                        flag=0;
                        MNT[j][3]=Integer.toString(k);
                        j++;
                    }
                }
                
			}
			fileReader.close();
            System.out.println("MNT:");
            l1=0;l2=0;
            for(l1=0;l1<j;l1++){
                for(l2=0;l2<4;l2++){
                    System.out.print(MNT[l1][l2]+" ");    
                }
                System.out.println();
            }
            System.out.println();
            
            System.out.println("MDT:");
            l1=0;
            for(l1=0;l1<k;l1++){
                System.out.println(MDT[l1]);
            }
            System.out.println();
            
            System.out.println("Formal vs Positional Parameter:");
            l1=0;l2=0;l3=0;
            System.out.println("-----------");
            for(l3=0;l3<k1;l3++){
                for(l1=0;l1<Integer.valueOf(MNT[l3][1]);l1++){
                    for(l2=0;l2<2;l2++){
                        System.out.print(FT[l1][l2][l3]+" ");    
                    }
                    System.out.println();
                }
                System.out.println("-----------");
            }
            System.out.println();
            
			//System.out.println(stringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}