import java.io.*;

//always use .equals to compare strings in java instead of ==
//SYMTAB - To access, start with 1 as symptr and not 0

//Done by - Vinit Vijan 151614 C19

public class assembler{
    public static void main(String[] args){
        try{
            File file = new File("ass2.asm");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str = "";
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					"test2.txt"), true));
            writer.write(str);

            String line,str1,str2;
            String OPTAB[][] = {{"STOP","1","0","1"},{"ADD","1","1","1"},{"SUB","1","2","1"},
            {"MULT","1","3","1"},{"MOVER","1","4","1"},{"MOVEM","1","5","1"},{"COMP","1","6","1"},
            {"BC","1","7","1"},{"DIV","1","8","1"},{"READ","1","9","1"},{"PRINT","1","10","1"},
            {"DC","2","0","1"},{"DS","2","1","1"},{"START","3","0","0"},{"ORIGIN","3","1","0"},
            {"LTORG","3","2","0"},{"EQU","3","3","0"},{"END","3","4","0"}};
            String SYMTAB[][] = new String[10][2];
            String LITTAB[][] = new String[10][2];
            String POOLTAB[][] = new String[10][2];
            LITTAB[0][0] = "-";LITTAB[0][1] = "-";        //wont deal with the 0th index, mostly
            POOLTAB[0][0] = "1";POOLTAB[0][1] = "0";        //wont deal with the 0th index, mostly
            POOLTAB[1][0] = "1";POOLTAB[1][1] = "0";
            int m=0,i1=0,symptr=0,LC=0,i=0,flag2=0,litptr=1,poolptr=1,temp_cl,m1=0,t1=0,t2=0,flag3=0,addr=0,incr=0,count=0,j=0,i2=0,instsize=0,litptr2=1,poolptr2=1;
                
            while ((line = bufferedReader.readLine()) != null) {
                String[] w = line.split("   ");
                m = w.length;
                //for(i1=0;i1<m;i1++){
                //    System.out.println(w[i1]);
                //}
                i1=0;
                //CHECKING LABEL
                if(w[0]!=""){       //label/symbol exists
                    flag2=0;
                    for(i=0;i<symptr;i++){
                        if(w[0].equals(SYMTAB[i][0])){
                            flag2=1;      //it was a symbol - put in the LC as its address
                            i1=i;
                            break;
                        }
                    }
                    if(flag2==1){
                        SYMTAB[i1][1]=Integer.toString(LC);
                    }
                    else{       //it was a label, put it in the symtab
                        SYMTAB[symptr][0]=w[0];
                        SYMTAB[symptr][1]=Integer.toString(LC);
                        //System.out.println("Im a label, symptr-"+symptr+" r-"+SYMTAB[symptr][0]+" "+SYMTAB[symptr][1]);
                        symptr++;
                    }
                }
                
                i1=0;
                //w[1] -> search in OPTAB, get the row no in i1.
                for(i=0;i<18;i++){
                    if(w[1].equals(OPTAB[i][0])){
                        i1=i;
                        break;
                    }
                }
                temp_cl = Integer.valueOf(OPTAB[i1][1]);
                if(temp_cl==1){     //Imperative statement
                    if(w[1].equals("BC")){
                        LC++;
                    }
                    else if(w[1].equals("STOP")){
                        LC++;
                    }
                    //not doing read and print for now
                    else{       //dealing with mnemonics with 2 operands here
                        String opr[] = w[2].split(",");
                        m1 = opr.length;        //has to be 2
                        str1 = opr[1].substring(0,1);
                        flag3=0;
                        if(str1.equals("=")){       //operand2 was a literal
                            t1=Integer.valueOf(POOLTAB[poolptr][0]);
                            t2=litptr-1;
                            for(i=t1;i<=t2;i++){
                                if(opr[1].equals(LITTAB[i][0])){
                                    flag3=1;
                                    break;
                                }
                            }
                            if(POOLTAB[poolptr][1].equals("0")||flag3==0){      //the literal hasnt appeared b4
                                LITTAB[litptr][0]=opr[1];
                                LITTAB[litptr][1]="-";      //this is filled when LTORG is encountered
                                t1=Integer.valueOf(POOLTAB[poolptr][1]);
                                t1++;
                                POOLTAB[poolptr][1]=Integer.toString(t1);
                                litptr++;
                            }
                        }
                        else{       //operand2 was either a symbol or a constant
                            SYMTAB[symptr][0]=opr[1];
                            SYMTAB[symptr][1]="-";
                            //System.out.println("Im a symbol, symptr-"+symptr+" r-"+SYMTAB[symptr][0]+" "+SYMTAB[symptr][1]);
                            symptr++;
                        }

                        LC++;
                    }
                }
                else if(temp_cl==2){        //DC or DS
                    LC++;       //considering the size of all statements to be 1
                }
                else if(temp_cl==3){
                    if(w[1].equals("LTORG")){
                        //LITTAB[POOLTAB[poolptr][0]][1]=LC;
                        t1=Integer.valueOf(POOLTAB[poolptr][0]);
                        t2=litptr-1;
                        for(i=t1;i<=t2;i++){
                            LITTAB[i][1]=Integer.toString(LC);
                            LC++;
                        }
                        poolptr++;
                        POOLTAB[poolptr][0]=Integer.toString(litptr);
                        POOLTAB[poolptr][1]="0";
                    }
                    else if(w[1].equals("END")){
                        t1=Integer.valueOf(POOLTAB[poolptr][0]);
                        t2=litptr-1;
                        for(i=t1;i<=t2;i++){
                            LITTAB[i][1]=Integer.toString(LC);
                            LC++;
                        }
                        poolptr++;
                        POOLTAB[poolptr][0]=Integer.toString(litptr);
                        POOLTAB[poolptr][1]="0";
                        break;
                    }
                    else if(w[1].equals("START")){
                        LC=Integer.valueOf(w[2]);
                    }
                    else if(w[1].equals("ORIGIN")){
                        String par[] = w[2].split("\\+");
                        for(i=0;i<symptr;i++){
                            if(par[0].equals(SYMTAB[i][0])){
                                i1=i;
                                break;
                            }
                        }
                        addr=Integer.valueOf(SYMTAB[i1][1]);
                        incr=Integer.valueOf(par[1]);
                        addr+=incr;
                        LC=addr;
                    }
                    else if(w[1].equals("EQU")){
                        for(i=0;i<symptr-1;i++){
                            if(w[2].equals(SYMTAB[i][0])){
                                i1=i;
                                break;
                            }
                        }
                        //addr=Integer.valueOf(SYMTAB[i1][1]);
                        SYMTAB[symptr-1][1]=SYMTAB[i1][1];
                    }
                }

                
            }
            fileReader.close();
            
            FileReader fileReader1 = new FileReader(file);
			BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
            LC=0;poolptr2=1;litptr2=1;
            int MCCODE[][] = new int[30][4];        //machine code
            j=0;        //row no of MCCODE
            while ((line = bufferedReader1.readLine()) != null) {
                String[] w = line.split("   ");
                m = w.length;
                //no need of CHECKING the LABELS
                i1=0;
                //w[1] -> search in OPTAB, get the row no in i1.
                for(i=0;i<18;i++){
                    if(w[1].equals(OPTAB[i][0])){
                        i1=i;       //i1 stores the row no.
                        break;
                    }
                }
                temp_cl = Integer.valueOf(OPTAB[i1][1]);
                if(temp_cl==1){     //Imperative statement
                    if(w[1].equals("BC")){
                        MCCODE[j][0]=LC;
                        MCCODE[j][1]=7;
                        //MCCODE[j][2]=0;
                        String opr[] = w[2].split(",");
                        if(opr[0]=="LT"){
                            MCCODE[j][2]=1;
                        }
                        else if(opr[0]=="LE"){
                            MCCODE[j][2]=2;
                        }
                        else if(opr[0]=="EQ"){
                            MCCODE[j][2]=3;
                        }
                        else if(opr[0]=="GT"){
                            MCCODE[j][2]=4;
                        }
                        else if(opr[0]=="GE"){
                            MCCODE[j][2]=5;
                        }
                        else if(opr[0]=="ANY"){
                            MCCODE[j][2]=6;
                        }
                        for(i=0;i<symptr;i++){
                            if(opr[1].equals(SYMTAB[i][0])){
                                i1=i;
                                break;
                            }
                        }
                        addr=Integer.valueOf(SYMTAB[i1][1]);
                        MCCODE[j][3]=addr;
                        instsize=Integer.valueOf(OPTAB[i1][3]);
                        LC+=instsize;
                        //LC++;       //size of BC is 1
                    }
                    else if(w[1].equals("STOP")){
                        MCCODE[j][0]=LC;
                        MCCODE[j][1]=0;
                        MCCODE[j][2]=0;
                        MCCODE[j][3]=0;
                        instsize=Integer.valueOf(OPTAB[i1][3]);
                        LC+=instsize;
                        //LC++;       //size is 1
                    }
                    //not doing read and print for now
                    else{       //dealing with mnemonics with 2 operands here
                        String opr[] = w[2].split(",");
                        m1 = opr.length;        //has to be 2
                        MCCODE[j][0]=LC;
                        MCCODE[j][1]=Integer.valueOf(OPTAB[i1][2]);
                        //System.out.println(opr[0]);
                        if(opr[0].equals("AREG")){
                            MCCODE[j][2]=1;
                        }
                        else if(opr[0].equals("BREG")){
                            MCCODE[j][2]=2;
                        }
                        else if(opr[0].equals("CREG")){
                            MCCODE[j][2]=3;
                        }
                        else if(opr[0].equals("DREG")){
                            MCCODE[j][2]=4;
                        }

                        str1 = opr[1].substring(0,1);
                        flag3=0;
                        if(str1.equals("=")){       //operand2 was a literal
                            //t1=Integer.valueOf(POOLTAB[poolptr][0]);
                            t2=litptr-1;
                            for(i=1;i<=t2;i++){
                                if(opr[1].equals(LITTAB[i][0])){
                                    //flag3=1;
                                    i2=i;
                                    break;
                                }
                            }
                            MCCODE[j][3]=Integer.valueOf(LITTAB[i2][1]);
                        }
                        else{       //operand2 was either a symbol or a constant
                            for(i=1;i<symptr;i++){
                                if(opr[1].equals(SYMTAB[i][0])){
                                    i2=i;
                                    break;
                                }
                            }
                            MCCODE[j][3]=Integer.valueOf(SYMTAB[i2][1]);
                        }
                        instsize=Integer.valueOf(OPTAB[i1][3]);
                        LC+=instsize;
                    }
                }
                else if(temp_cl==2){        //DC or DS
                    MCCODE[j][0]=LC;
                    MCCODE[j][1]=0;     //keeping zero instead of blank to be on the safe side
                    MCCODE[j][2]=0;
                    MCCODE[j][3]=0;
                    LC++;       //considering the size of all statements to be 1
                }
                else if(temp_cl==3){
                    if(w[1].equals("LTORG")){
                        //LITTAB[POOLTAB[poolptr][0]][1]=LC;
                        //System.out.println("POOLTAB[poolptr][0]-"+POOLTAB[poolptr2][0]);
                        //System.out.println("POOLTAB[poolptr+1][0]-"+POOLTAB[poolptr2+1][0]);
                        t1=Integer.valueOf(POOLTAB[poolptr2][0]);
                        t2=Integer.valueOf(POOLTAB[poolptr2+1][0]);
                        t2--;
                        for(i=t1;i<=t2;i++){
                            MCCODE[j][0]=LC;
                            MCCODE[j][1]=0;
                            MCCODE[j][2]=0;
                            str2 = LITTAB[i][0].substring(2,3);
                            MCCODE[j][3]=Integer.valueOf(str2);
                            LC++;
                            if(i<t2){
                                j++;
                            }
                        }
                        poolptr2++;
                    }
                    else if(w[1].equals("END")){
                        //System.out.println("POOLTAB[poolptr][0]-"+POOLTAB[poolptr2][0]);
                        //System.out.println("POOLTAB[poolptr+1][0]-"+POOLTAB[poolptr2+1][0]);
                        t1=Integer.valueOf(POOLTAB[poolptr2][0]);
                        t2=Integer.valueOf(POOLTAB[poolptr2+1][0]);
                        t2--;
                        for(i=t1;i<=t2;i++){
                            MCCODE[j][0]=LC;
                            MCCODE[j][1]=0;
                            MCCODE[j][2]=0;
                            str2 = LITTAB[i][0].substring(2,3);
                            MCCODE[j][3]=Integer.valueOf(str2);
                            LC++;
                            if(i<t2){
                                j++;
                            }
                        }
                        poolptr2++;
                        break;
                    }
                    else if(w[1].equals("START")){
                        LC=Integer.valueOf(w[2]);
                        //MCCODE[j][0]=LC;      dont do this
                        j--;
                    }
                    else if(w[1].equals("ORIGIN")){
                        String par[] = w[2].split("\\+");
                        for(i=1;i<symptr;i++){
                            if(par[0].equals(SYMTAB[i][0])){
                                i2=i;
                                break;
                            }
                        }
                        addr=Integer.valueOf(SYMTAB[i2][1]);
                        incr=Integer.valueOf(par[1]);
                        addr+=incr;
                        LC=addr;
                        j--;
                    }
                    else if(w[1].equals("EQU")){
                        j--;
                    }
                }

                j++;
            }


            fileReader1.close();
            writer.close();

            System.out.println("SYMBOL TABLE:");
            System.out.println("Symbol  Address");
            t1=0;t2=0;
            for(t1=1;t1<symptr;t1++){
                for(t2=0;t2<2;t2++){
                    System.out.print(SYMTAB[t1][t2]+"     ");    
                }
                System.out.println();
            }
            System.out.println();

            System.out.println("LITERAL TABLE:");
            System.out.println("Literal  Address");
            t1=0;t2=0;
            for(t1=1;t1<litptr;t1++){
                for(t2=0;t2<2;t2++){
                    System.out.print(LITTAB[t1][t2]+"     ");    
                }
                System.out.println();
            }
            System.out.println();

            System.out.println("POOL TABLE:");
            System.out.println("First  #Literal");
            t1=0;t2=0;
            for(t1=1;t1<=poolptr;t1++){
                for(t2=0;t2<2;t2++){
                    System.out.print(POOLTAB[t1][t2]+"       ");    
                }
                System.out.println();
            }
            System.out.println();

            System.out.println("M/C Code TABLE:");
            System.out.println("LC  Opcode  Register  Mem-Operand");
            t1=0;t2=0;
            for(t1=0;t1<=j;t1++){
                for(t2=0;t2<4;t2++){
                    System.out.print(MCCODE[t1][t2]+"      ");    
                }
                System.out.println();
            }
            System.out.println();
        }
        catch (IOException e) {
			e.printStackTrace();
		}

    }
}