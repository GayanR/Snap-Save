package billfiltering;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nuwan on 6/14/2015.
 */
public class FilterAlgo {

        public String[][] filter( String bill_lines) {

            //TODO write your implementation code here:
            ArrayList<String> nonemptylist=new ArrayList<String>();
            ArrayList<String> itemnamelist=new ArrayList<String>();
            ArrayList<String> quantitylist=new ArrayList<String>();
            ArrayList<String> subtotallist=new ArrayList<String>();
            String s[]=bill_lines.split("\n");// splitting the receipt into lines
            String results[][]=new String[9][];
            int itemnameposition=-1;
            int quantityposition=-1;
            int subtotalposition=-1;
            int columnend=0;
            String regex="";
            String result="";// result to be returned
            int x;
            String errorarr[]=new String[1];
            errorarr[0]="No Error";
            results[0]=errorarr;
            for( x=0;x<s.length;x++)// printing the entire bill
            {
                System.out.println(s[x]);
                result=result+s[x]+"\n";
            }


            for( x=0;x<s.length;x++)// printing the entire bill
            {
                if(!s[x].trim().isEmpty())
                {
                    s[x]=s[x].replaceAll("\\*","");// removing stars from the bill
                    nonemptylist.add(s[x]);
                }
            }
            s=nonemptylist.toArray(new String[nonemptylist.size()]);
            for( x=0;x<s.length;x++)// printing the entire bill
            {
                System.out.println(s[x]);
                result=result+s[x]+"\n";
            }
            x=0;
            while(!s[x].matches("[a-zA-Z0-9 ']*"))
            {
                x++;
                if(x>=s.length)
                {
                    break;
                }
            }
            //getting the shop name
            System.out.println("shop name is "+s[x]);
            String shoparr[]=new String[1];
            shoparr[0]=s[x];
            results[1]=shoparr;
            result=result+"shop name is  "+s[x]+"\n";
            String date="";// storing the date of the receipt
            String time="";// storing the time of the receipt
            String word[];
            String[][] column={};
            boolean date_matched=false;//finding the date of the receipt
            boolean time_matched=false;//finding the time of the receipt
            boolean column_found=false;//finding  end of the columns pf the receipt
            for( x=0;x<s.length;x++)
            {

                word=s[x].split("\\s+");// removing whitespaces for each row

                //SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy",Locale.US);
                //SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd",Locale.US);

                if(date_matched&&time_matched&&column_found)//check all fields are found
                {
                    break;
                }
                if(!date_matched||!time_matched){
                    for (String word1 : word) {




                        if(!date_matched)
                        {
                            date=Find_date(word1);
                            if(!date.isEmpty())
                            {
                                date_matched=true;
                            }
                        }

                        if(!time_matched)
                        {
                            time=Find_time(word1);
                            if(!time.isEmpty())
                            {
                                time_matched=true;
                            }
                        }
                    }
                }

                if(!column_found&&ColumnEnd(word))// getting column names if available
                {
                    columnend=x;
                    System.out.println(s[x]);
                    result=result+"column names "+s[x]+"\n";
                    if(check_columnwordpool(s[x]))
                    {
                        result=result+"words are in the pool \n";
                        column=get_pool_expressions(s[x]);
                        for(int a=0;a<column.length;a++)
                        {
                            if(regex.isEmpty()){
                                regex=regex+column[a][1];
                            }
                            else
                            {

                                regex=regex+"\\s+"+column[a][1];

                            }
                            regex=regex.trim();
                            for(int b=0;b<column[a].length;b++)
                            {
                                System.out.println(column[a][b]);
                            }
                            if(itemnameposition==-1&&column[a][0].equalsIgnoreCase("Item"))
                            {
                                itemnameposition=a;
                            }
                            if(quantityposition==-1&&column[a][0].equalsIgnoreCase("Qty"))
                            {
                                quantityposition=a;
                            }
                            if(subtotalposition==-1&&column[a][0].equalsIgnoreCase("Amount"))
                            {
                                subtotalposition=a;
                            }
                        }
                        System.out.println(regex);
                    }
                    column_found=true;
                }


            }
            if(itemnameposition==-1||quantityposition==-1||subtotalposition==-1)
            {
                errorarr[0]="Template Doesnt match";
                results[0]=errorarr;

                return results;
            }

            result=result+"the date is "+date
                    +"\nthe time is " +time
                    +"\n";
            String datearr[]=new String[1];
            datearr[0]=date;
            results[2]=datearr;
            String timearr[]=new String[1];
            timearr[0]=time;
            results[3]=timearr;
            int incrementer=1;
            int attempt=1;
            for(;columnend+incrementer<s.length;columnend=columnend+incrementer)
            {
                String line="";
                for(int at=1;at<=incrementer;at++)
                {
                    line=line+" "+s[columnend+at];
                    line=line.trim().replaceAll("\\b(?i)X\\b", "");
                    line=line.trim().replaceAll("\\b(?i)Rs\\b\\s*", "");
                    line=line.trim().replaceAll(",","");
                }
                String[] row;
                row=extract_from_regex(line, regex, column.length);

                if(row.length==column.length)
                {
                    System.out.println("next row : ");
                    for(String item:row)
                    {
                        System.out.println(item);
                    }
                    itemnamelist.add(row[itemnameposition]);
                    quantitylist.add(row[quantityposition]);
                    subtotallist.add(row[subtotalposition]);
                    attempt++;

                }
                else if(attempt==1)
                {

                    for(;columnend+incrementer+1<s.length;)
                    {
                        incrementer++;
                        line=line+" "+s[columnend+incrementer];
                        line=line.trim().replaceAll("\\b(?i)X\\b", "");
                        line=line.trim().replaceAll("\\b(?i)Rs\\b\\s*", "");
                        line=line.trim().replaceAll(",", "");

                        row=extract_from_regex(line, regex, column.length);
                        if(row.length==column.length)
                        {
                            System.out.println("first row : ");
                            for(String item:row)
                            {
                                System.out.println(item);
                            }
                            itemnamelist.add(row[itemnameposition]);
                            quantitylist.add(row[quantityposition]);
                            subtotallist.add(row[subtotalposition]);
                            System.out.println("End of first row");
                            attempt++;
                            break;
                        }



                    }
                    if(incrementer>3)
                        System.out.println("no match found :(");

                }
                else
                {
                    break;
                }

            }
            columnend=columnend-incrementer;
            Pattern pattern1 = Pattern.compile("Gross Amount\\s+(\\d+(?:\\.\\d+)?)");
            Pattern pattern2 = Pattern.compile("Net Total\\s+(\\d+(?:\\.\\d+)?)");
            Pattern pattern3 = Pattern.compile("Payable Total\\s+(\\d+(?:\\.\\d+)?)");
            String total = "";
            String totalarr[] = new String[1];
            totalarr[0] = total;
            Scanner sc;
            for (; columnend < s.length; columnend++) {
                s[columnend] = s[columnend].replaceAll(",", "");
                s[columnend] = s[columnend].replaceAll("Rs", "");
                s[columnend] = s[columnend].replaceAll(":", "");
                Matcher matcher1 = pattern1.matcher(s[columnend]);
                Matcher matcher2 = pattern2.matcher(s[columnend]);
                Matcher matcher3 = pattern3.matcher(s[columnend]);
                if (matcher1.find()) {
                    total = matcher1.group(0);
                    sc = new Scanner(total);
                    while (sc.findWithinHorizon(pattern1, 0) != null) {
                        MatchResult mr = sc.match();
                        total=mr.group(1);
                    }


                    total = total.trim();

                    totalarr[0] = total;

                    System.out.println("Total : " + total);
                    break;

                } else if (matcher2.find()) {
                    total = matcher2.group(0);
                    sc = new Scanner(total);
                    while (sc.findWithinHorizon(pattern2, 0) != null) {
                        MatchResult mr = sc.match();
                        total=mr.group(1);
                    }

                    total = total.trim();

                    totalarr[0] = total;

                    System.out.println("Total : " + total);
                    break;
                }else if (matcher3.find()) {
                    total = matcher3.group(0);
                    sc = new Scanner(total);
                    while (sc.findWithinHorizon(pattern3, 0) != null) {
                        MatchResult mr = sc.match();
                        total=mr.group(1);
                    }

                    total = total.trim();

                    totalarr[0] = total;

                    System.out.println("Total : " + total);
                    break;
                }

            }
            results[4]=totalarr;
            results[5]=itemnamelist.toArray(new String[itemnamelist.size()]);
            results[6]=quantitylist.toArray(new String[quantitylist.size()]);
            results[7]=subtotallist.toArray(new String[subtotallist.size()]);



            //            try{
            //            DbAccess.getData("SELECT * from sample");
            //            }
            //            catch(SQLException e)
            //            {
            //                System.out.println("SQL exception caught:"+e.getMessage());
            //            }
            //            catch(ClassNotFoundException c)
            //            {
            //                System.out.println("Class not found exception caught"+c.getMessage());
            //            }



            return results;
        }


        public boolean ColumnEnd(String word[])
        {
            return word[word.length-1].equalsIgnoreCase("Amount");

        }


    public String Find_date(String word) {
        String date = "";
        Matcher matcher;
        Pattern pattern1 = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
        Pattern pattern2 = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Pattern pattern3= Pattern.compile("\\d{2}/\\d{2}/\\d{2}");
        matcher = pattern1.matcher(word);
        if (matcher.find()) {
            date = matcher.group(0);
            System.out.println(date + " pattern matched");
            return date;
        }
        matcher = pattern2.matcher(word);
        if (matcher.find()) {
            date = matcher.group(0);
            System.out.println(date + " pattern matched");
            return date;
        }
        matcher = pattern3.matcher(word);
        if (matcher.find()) {
            date = matcher.group(0);
            System.out.println(date + " pattern matched");
            return date;
        }
        return date;
    }


        public String Find_time(String word)
        {
            Pattern pattern1 = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
            Pattern pattern2 = Pattern.compile("\\d{2}:\\d{2}");
            Matcher matcher;
            String time="";
            matcher= pattern1.matcher(word);
            if (matcher.find())
            {
                time=matcher.group(0);
                System.out.println(time+" pattern matched");
                return time;
            }
            matcher= pattern2.matcher(word);

            if (matcher.find())
            {

                time=matcher.group(0);
                System.out.println(time+" pattern matched");
                return time;
            }
            return time;
        }

        public boolean check_columnwordpool(String row)
        {
            String columnwordpool[]={"Ln","NO","Item","Price","Qty","Amount"};
            for(String column:columnwordpool)
            {
                row=row.replaceFirst( "\\b(?i)"+column+"\\b","");// match words ignoring the case
                row=row.trim();
                if(row.isEmpty())
                {
                    return true;// column names successfully matched from the column pool
                }
            }
            return false;//column names given are not in the pool
        }

        public String[][] get_pool_expressions(String columnnames)
        {
            String[][] expressions={
                    {"Ln","(\\d+)"},
                    {"NO","(\\d+)"},
                    {"Item","(.*)"},
                    {"Price","(\\d+(?:\\.\\d+)?)"},
                    {"Qty","(\\d+(?:\\.\\d+)?)"},
                    {"Amount","(\\d+(?:\\.\\d+)?)"}
            };
            columnnames=columnnames.trim();

            List<String> match=new ArrayList<String>();
            List<String> matchexp=new ArrayList<String>();
            String newcolumn="";
            int pool_length =expressions.length;
            System.out.println(pool_length);
            for(int a=0;a<pool_length;a++)
            {
                for(int b=0;b<pool_length;b++)
                {
                    //System.out.println(expressions[b][0]);
                    //System.out.println(expressions[b][1]);
                    newcolumn=columnnames.replaceFirst( "\\b(?i)"+expressions[b][0]+"\\b$","");// match words ignoring the case
                    newcolumn=newcolumn.trim();
                    System.out.println(newcolumn);
                    if( !newcolumn.equals(columnnames))
                    {
                        columnnames=newcolumn;
                        match.add(expressions[a][0]);
                        matchexp.add(expressions[a][1]);
                        break;
                        // column name successfully matched from the column pool
                    }


                }
            }
            String[][] matched=new String[match.size()][2];
            System.out.println(match.size());
            for(int x=0;x<match.size();x++)
            {
                matched[x][0]=match.get(x);
                matched[x][1]=matchexp.get(x);
            }
            return matched;
        }

        public String[] extract_from_regex(String row,String regex,int length)
        {
            System.out.println(row);
            String[] resultrow=new String[length];

            Scanner sc = new Scanner(row);
            Pattern p = Pattern.compile(regex);
            while (sc.findWithinHorizon(p, 0) != null) {
                MatchResult mr = sc.match();
                for(int i=0;i<length;i++)
                {
                    resultrow[i]=mr.group(i+1);
                }
            }
            if(resultrow[0]==null)
            {
                String[] onevalue={"no value"};
                return onevalue;
            }
            return resultrow;
        }


}
