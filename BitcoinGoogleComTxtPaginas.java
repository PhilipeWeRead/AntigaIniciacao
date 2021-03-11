package BitcoinGoogleComTxtPaginas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BitcoinGoogleComTxtPaginas {
    public static List<Date> getDaysBetweenDatesIncludeFinalDate(Date startdate, Date enddate, boolean includeFinalDate) {
	    List<Date> dates = new ArrayList<Date>();
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(startdate);

	    while (calendar.getTime().before(enddate)) {
	        Date result = calendar.getTime();
	        dates.add(result);
	        calendar.add(Calendar.DATE, 1);
	    }	    
	    
	    if(includeFinalDate) {
	    	
	    	Date result = calendar.getTime();
	    	dates.add(result);
	    }
	    
	    return dates;
	}
    
    public static String getMonth(String m) {
        String month = "";
        if (m.toLowerCase().equals("jan")) month = "01";
        if (m.toLowerCase().equals("feb")) month = "02";
        if (m.toLowerCase().equals("mar")) month = "03";
        if (m.toLowerCase().equals("apr")) month = "04";
        if (m.toLowerCase().equals("may")) month = "05";
        if (m.toLowerCase().equals("jun")) month = "06";
        if (m.toLowerCase().equals("jul")) month = "07";
        if (m.toLowerCase().equals("aug")) month = "08";
        if (m.toLowerCase().equals("sep")) month = "09";
        if (m.toLowerCase().equals("oct")) month = "10";
        if (m.toLowerCase().equals("nov")) month = "11";
        if (m.toLowerCase().equals("dec")) month = "12";
        
        return month;    
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        
        String address = "https://www.google.com.br/search?";
        String charset = "UTF-8";
        String language = "en";
        Reader reader = null;
        String keyword = "bitcoin";       
        Date from = new Date("07/13/2012");
        Date to = new Date("07/13/2013");
        
        List<Date> dates = getDaysBetweenDatesIncludeFinalDate(from, to, true);
        List<String> daysToSearch = new ArrayList();
        
        for(Date data : dates)
        {
            String[] date = data.toString().split(" ");
            daysToSearch.add(getMonth(date[1])+"/"+date[2]+"/"+date[5]);
        }
    
        String initialDate = daysToSearch.get(0);
        String finalDate = daysToSearch.get(daysToSearch.size()-1);
        
        Random time = new Random();
        //get the range, casting to long to avoid overflow problems
        long range = 60000 - 50001;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * time.nextDouble());
        int randomNumber =  (int)(fraction + 100000); 
        System.out.println("Wait for "+randomNumber);
        
        String url_str = address+"hl="+language+"&tbm=nws&as_q="+keyword+"&as_occt=any&as_drrb=b&tbs=cdr:1,cd_min:"+initialDate+",cd_max:"+finalDate+"&authuser=0&gws_rd=cr&ei=keX6VvqQOIbGwASowKmABg#q="+keyword+"&hl="+language+"&gl="+language+"&as_drrb=b&authuser=0&tbs=cdr:1,cd_min:"+initialDate+",cd_max:"+finalDate+",sbd:1&tbm=nws";
        System.out.println(url_str);
        Document doc = Jsoup.connect(url_str).timeout(randomNumber).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36").get();
               
        File arquivo = new File("C:\\Users\\phili\\Desktop\\bitcoin.txt");
        
        try {
        	 
        	if (!arquivo.exists()) {
        	//cria um arquivo (vazio)
        	arquivo.createNewFile();
        	}
        	
        	FileWriter fw = new FileWriter(arquivo, true);     	 
        	BufferedWriter bw = new BufferedWriter(fw);
        	
        	int count = 0;       
        
        while(count != 14) {	
        Elements links = doc.select("#rso .g"); //Parâmetro de captura do bloco de páginas
        Elements linksPage = doc.select("#pnnext");
                        
        for(Element element: links){ 
            
            Elements l = element.select(".gG0TJc");
            //System.out.println(l.text()); Usar caso esteja na dúvida sobre a captura dos elementos
            if(!l.isEmpty())
            {
                Elements titlee = element.select(".l.lLrAF");
                String title = titlee.text();
                System.out.println("Title: "+title);
                bw.write(title); bw.newLine();
                
                Elements urle = element.select(".l.lLrAF");
                String url = urle.attr("href");              
                System.out.println("Link: "+url);
                bw.write(url); bw.newLine();
                
                Elements publisher = element.select(".slp .xQ82C.e8fRJf"); //.slp Certo
                String publisherName = publisher.text();
                System.out.println("Source: "+publisherName);
                bw.write(publisherName); bw.newLine();
                
                Elements date = element.select(".slp .f.nsa.fwzPFf"); //.slp Certo
                String publishedDate = date.text();
                System.out.println("Date: "+publishedDate);
                bw.write(publishedDate); bw.newLine();
                
                Elements description = element.select(".st"); //.st Certo 
                String abstract_ = description.text();
                System.out.println("Description: "+abstract_);
                bw.write(abstract_); bw.newLine();
                
                //String dateOfPublicationString = publishedDate.replaceAll(" de", "");
                //String[] dateOfPulicationVector = dateOfPublicationString.split(" ");
                //String dateOfPublication = dateOfPulicationVector[2]+"-"+getMonth(dateOfPulicationVector[0])+"-"+dateOfPulicationVector[1];
               
                System.out.println("\n"); bw.newLine();                 
            }
            
        }
        
        randomNumber = time.nextInt(10) * 10; 
        try {
        	Thread.sleep(randomNumber);
        }catch(InterruptedException ex) {
        	System.out.println(ex);
        }
        
        //======================= Geraçao de um novo aleatorio
        fraction = (long)(range * time.nextDouble());
        randomNumber =  (int)(fraction + 100000); 
          
        String linksPages = linksPage.attr("href");
        doc = Jsoup.connect("https://www.google.com.br"+linksPages).timeout(randomNumber).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36").get();
    	System.out.println("Pagina Numero: "+(count+1)+"\n"+linksPages+"\n\n");
        count++;
        
        }
        
        bw.close();
        fw.close();
        
        //System.out.println("Pressione Enter para continuar..."); 
        //System.in.read(); 
        //System.out.println("Fim.");
        
        }catch (IOException ex) {
        	ex.printStackTrace();
        }
             
    }
    
}