package br.com.pedro.urlParser;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	String content = getHttpContent(args[0]);
    	String result = inspectHttpContent(content);
    	
    	System.out.println(result);
    }

    /**
     * Busca o conteudo da Url e retorna uma string com o Conteúdo HTML da url via BufferedReader 
     * @param urlString url a buscar o conteúdo. 
     * @return String do Conteudo html vindo da url em caso de sucesso
     * @throws Exception
     */
    public static String getHttpContent(String urlString) throws Exception{
        StringBuilder httpContent = new StringBuilder();
        
        if(urlString != null){
            try {
                URL url = new URL(urlString);
                
                URLConnection urlConnection = url.openConnection();
                
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                
                /**
                 * Le o transfere o conteudo da Url para o htmlContent via bufferedReader
                 */
                String line;  
                while ((line = bufferedReader.readLine()) != null){  
                  httpContent.append(line + "\n");  
                }  
                bufferedReader.close();  
                
            } catch (Exception e) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
                
                throw e;
            }
        }
        
        return httpContent.toString();
    }
    
    /**
     * Busca por uma lista de Keywords em uma string usando Regex. 
     * @param httpContent String do conteudo do site 
     * @return "suspicious" no caso de encontrar alguma das palavras ou "safe" caso não seja encontrado. 
     * @throws Exception 
     */
    public static String inspectHttpContent(String httpContent) throws Exception{
    	StringBuilder httpSafety = new StringBuilder();
    	
    	try {
    		/**
    		 * Cria uma lista de palavras-chave para usar um Regex
    		 */
    		List<String> keywords = new ArrayList<String>();
        	keywords.add("black friday");
        	keywords.add("promocao");
        	keywords.add("senha");
        	
        	/**
        	 * Constroi o padrão regex para achar as palavras chave
        	 */
        	StringBuilder patternString = new StringBuilder();
        	patternString.append("\\b(").append(String.join("|", keywords)).append(")\\b");
        	
        	/**
        	 * Usa o Matcher para buscar o regex pattern
        	 */
        	Pattern pattern = Pattern.compile(patternString.toString());
        	Matcher matcher = pattern.matcher(httpContent.toLowerCase());
        	
        	/**
        	 * Em caso de achar o pattern, retorna suspicious. Caso contrário, retorna safe
        	 */
        	boolean matchFound = matcher.find();
        	if(matchFound) {
        		httpSafety.append("suspicious");
        	}
        	else {
        		httpSafety.append("safe");
        	}
        	
        	return httpSafety.toString();
    	}
    	catch(Exception e) {
    		Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
    	}
    	
    	return httpSafety.toString();
    }
}