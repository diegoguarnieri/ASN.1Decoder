package lib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import javax.smartcardio.ATR;

import bean.ASNSchema;

public class ASNSchemaLoad {

	public static HashMap<String, ASNSchema> parser(String diretorio, String arquivo) {
		HashMap<String, ASNSchema> asnSchema = new HashMap<String, ASNSchema>();
		HashMap<Integer, String> rawFile = new HashMap<Integer, String>();

		try {
			BufferedReader bf = new BufferedReader(new FileReader(diretorio + arquivo));

			int i = 0;
			String linha;
			while((linha = bf.readLine()) != null) {
				linha = linha.trim();

				if(!linha.equals("") && !linha.startsWith("-")) {
					//System.out.println(linha);
					rawFile.put(i, linha);
					i++;
				}

			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		Pattern padraoCabecalho = Pattern.compile("(.+)\\s+::=\\s+(.+)");
		Pattern padraoProperty = Pattern.compile("(.+?)(\\(.+)");
		Pattern padraoAttribute = Pattern.compile(
				"(.+)\\s+\\[(.+)\\]\\s+(.+)\\s+(.+)\\,*|" //firstmccmnc [192] MCCMNC OPTIONAL,
				+ "(.+)\\s+\\[(.+)\\]\\s+(.+)\\,*|" 
				+ "(.+)\\s+\\((.+)\\)\\,*|"
				+ "(.+)\\s+(.+)\\,*");

		ASNSchema schemaAtual = new ASNSchema();
		boolean estrutura = false;
		for(int i = 0; i < rawFile.size(); i++) {
			String input = rawFile.get(i).trim();

			/*if(input.startsWith("maxISDN-AddressLength")) {
				System.out.println("");
			}*/
			
			if(!estrutura) {
				//verifica se a linha atual eh cabecalho
				Matcher pesquisaCabecalho = padraoCabecalho.matcher(input);

				if(pesquisaCabecalho.find()) {
					String name = pesquisaCabecalho.group(1).trim();
					String property = pesquisaCabecalho.group(2).trim();

					if(property.substring(property.length() - 1, property.length()).equals("{")) {
						estrutura = true;
						property = property.replace("{", "").trim();
						//System.out.println("======>Estrutura aberta");
					} else {
						//se a linha atual nao possuir tag de inicio, a proxima pode possuir
						if(rawFile.get(i + 1).trim().equals("{")) {
							estrutura = true;
							i++;
							//System.out.println("======>Estrutura aberta");
						}
					}

					Matcher pesquisaProperty = padraoProperty.matcher(property);

					String type = "";
					String length = "";
					if(pesquisaProperty.find()) {
						type = pesquisaProperty.group(1).trim();
						length = pesquisaProperty.group(2).trim();
					} else {
						type = property;
					}
					length = length.replace("SIZE", "").replace("(", "").replace(")", "").trim();

					//System.out.println("Name: " + name + " =>Type: " + type + " =>Val: " + length);

					ASNSchema schema = new ASNSchema();
					schema.setName(name);
					schema.setType(type);
					schema.setLength(length);

					asnSchema.put(name, schema);

					schemaAtual = schema;	
				} else {
					//elementos sem estrutura
					//System.out.println(input);
				}
			} else {
				//estrutura esta aberta

				if(input.startsWith("}")) {
					estrutura = false;
					//System.out.println("======>Estrutura fechada");
				} else {
					//System.out.println("-------->" + input);

					input = input.replace("SEQUENCE OF", "").trim();
					input = input.replace("EXPLICIT", "").trim(); //parametro nao tratado nessa implementacao
					
					Matcher pesquisaAttribute = padraoAttribute.matcher(input);
					
					if(pesquisaAttribute.find()) {
						String name = "";
						String pos = "";
						String length = "";
						String type = "";
						String mandatory = "";

						try{ name = pesquisaAttribute.group(1).trim(); } catch(Exception e) { }
						try{ pos = pesquisaAttribute.group(2).trim(); } catch(Exception e) { }
						try{ type = pesquisaAttribute.group(3).trim(); } catch(Exception e) { }
						try{ mandatory = pesquisaAttribute.group(4).trim(); } catch(Exception e) { }

						try{ name = pesquisaAttribute.group(5).trim(); } catch(Exception e) { }
						try{ pos = pesquisaAttribute.group(6).trim(); } catch(Exception e) { }
						try{ type = pesquisaAttribute.group(7).trim(); } catch(Exception e) { }

						try{ name = pesquisaAttribute.group(8).trim(); } catch(Exception e) { }
						try{ length = pesquisaAttribute.group(9).trim(); } catch(Exception e) { }

						try{ name = pesquisaAttribute.group(10).trim(); } catch(Exception e) { }
						try{ type = pesquisaAttribute.group(11).trim(); } catch(Exception e) { }

						name = name.replace(",", "").trim();
						//type = type.replace(",", "").replace("SEQUENCE OF", "").trim();
						type = type.replace(",", "").trim();
						mandatory = mandatory.replace(",", "").trim();
						length = length.replace(",", "").trim();

						//System.out.println("-->Name: " + name + " -->Pos: " + pos + " -->Type: " + type + " -->Mandatory: " + mandatory + " -->Length: " + length);

						ASNSchema attribute = new ASNSchema();
						attribute.setName(name);
						attribute.setType(type);
						attribute.setLength(length);
						if(mandatory.equals("OPTIONAL")) {
							attribute.setMandatory(false);	
						}

						int key = 1;

						if(!pos.equals("")) {
							key = Integer.parseInt(pos);
						}

						schemaAtual.setAttribute(key, attribute);

					} else {
						System.out.println("Nao enontrado(" + i + "): " + input);
					}
				}
			}
		}
		
		return asnSchema;
	}
	
	//esse metodo retorna todos os nos roots do schema, ou seja,
	//todos os nos que nao tem pai, dentro de um schema pode haver mais de no root
	public static String buscaRoot(HashMap<String, ASNSchema> asnSchema) {

		for(Map.Entry<String, ASNSchema> asnName : asnSchema.entrySet()) {
			String nameBusca = asnName.getKey();

			boolean filho = false;
			for(Map.Entry<String, ASNSchema> asn : asnSchema.entrySet()) {
				String name = asn.getKey();
				
				if(!nameBusca.equals(name)) {
					HashMap<Integer, ASNSchema> attribute = asn.getValue().getAttribute();
					
					for(Map.Entry<Integer, ASNSchema> attributes : attribute.entrySet()) {
						String attributeType = attributes.getValue().getType();
						
						if(nameBusca.equals(attributeType)) {
							filho = true;
							break;
						}
					}
				}
				
				if(filho == true) {
					break;
				}	
			}
			
			if(filho == false) {
				System.out.println(nameBusca);
			}
		}

		return null;
	}
}