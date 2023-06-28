import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Lab1 extends JFrame implements ActionListener {
	static final long serialVersionUID = 1l;
	private JTextField assemblerInstruction;
	private JTextField binaryInstruction;
	private JTextField hexInstruction;
	private JLabel errorLabel;
	
	public Lab1() {
		setTitle("XDS Sigma 9");
		setBounds(100, 100, 400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
// * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// SET UP THE ASSEMBLY LANGUAGE TEXTFIELD AND BUTTON
		assemblerInstruction = new JTextField();
		assemblerInstruction.setBounds(25, 24, 134, 28);
		getContentPane().add(assemblerInstruction);

		JLabel lblAssemblyLanguage = new JLabel("Assembly Language");
		lblAssemblyLanguage.setBounds(30, 64, 160, 16);
		getContentPane().add(lblAssemblyLanguage);

		JButton btnEncode = new JButton("Encode");
		btnEncode.setBounds(200, 25, 117, 29);
		getContentPane().add(btnEncode);
		btnEncode.addActionListener(this);
// * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// SET UP THE BINARY INSTRUCTION TEXTFIELD AND BUTTON
		binaryInstruction = new JTextField();
		binaryInstruction.setBounds(25, 115, 330, 28);
		getContentPane().add(binaryInstruction);

		JLabel lblBinary = new JLabel("Binary Instruction");
		lblBinary.setBounds(30, 155, 190, 16);
		getContentPane().add(lblBinary);

		JButton btnDecode = new JButton("Decode Binary");
		btnDecode.setBounds(200, 150, 150, 29);
		getContentPane().add(btnDecode);
		btnDecode.addActionListener(this);
// * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// SET UP THE HEX INSTRUCTION TEXTFIELD AND BUTTON
		hexInstruction = new JTextField();
		hexInstruction.setBounds(25, 220, 134, 28);
		getContentPane().add(hexInstruction);

		JLabel lblHexEquivalent = new JLabel("Hex Instruction");
		lblHexEquivalent.setBounds(30, 260, 131, 16);
		getContentPane().add(lblHexEquivalent);

		JButton btnDecodeHex = new JButton("Decode Hex");
		btnDecodeHex.setBounds(200, 220, 150, 29);
		getContentPane().add(btnDecodeHex);
		btnDecodeHex.addActionListener(this);		
// * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// SET UP THE LABEL TO DISPLAY ERROR MESSAGES
		errorLabel = new JLabel("");
		errorLabel.setBounds(25, 320, 280, 16);
		getContentPane().add(errorLabel);
	}

	public void actionPerformed(ActionEvent evt) {
		errorLabel.setText("");
		if (evt.getActionCommand().equals("Encode")) {
			encode();
		} else if (evt.getActionCommand().equals("Decode Binary")) {
			decodeBin();
		} else if (evt.getActionCommand().equals("Decode Hex")) {
			decodeHex();
		}
	}

	public static void main(String[] args) {
		Lab1 window = new Lab1();
		window.setVisible(true);
	}

// USE THE FOLLOWING METHODS TO CREATE A STRING THAT IS THE
// BINARY OR HEX REPRESENTATION OF A SORT OR INT

// CONVERT AN INT TO 8 HEX DIGITS
	String displayIntAsHex(int x) {
		String ans="";
		for (int i=0; i<8; i++) {
			int hex = x & 15;
			char hexChar = "0123456789ABCDEF".charAt(hex);
			ans = hexChar + ans;
			x = (x >> 4);
		}
		return ans;
	}

// CONVERT AN INT TO 32 BINARY DIGITS
	String displayIntAsBinary(int x) {
		String ans="";
		for(int i=0; i<32; i++) {
			ans = (x & 1) + ans;
			x = (x >> 1);
		}
		return ans;
	}
	
/************************************************************************/
/* Put your implementation of the encode, decodeBin, and decodeHex      */
/* methods here. You may add any other methods that you think are       */
/* appropriate. However, you MUST NOT change anything in the code       */
/* that I have written.                                                 */
/************************************************************************/
	void encode() {
		String assemblyInput = assemblerInstruction.getText().trim(); 
		long machine = 0;
		int commaInput = assemblyInput.indexOf(","); //this just gives us the index doesn't store the value in it
		try { //INVALID FIRST PART EXCEPTION
		String operation = assemblyInput.substring(0,commaInput); //operation= stores anything from the index 0 and before the comma (the letters)

		int spaceIndex = assemblyInput.indexOf(" "); //Index of the last space
		int asteriskIndex = assemblyInput.indexOf('*');//index of asterisk
		int secondComma = assemblyInput.lastIndexOf(","); //index of second comma
		String indexR = assemblyInput.substring(commaInput+1, spaceIndex);// stores the value between the comma and the space so basically R
	
		String indexD = "";
		String indexX = "";
		String indexV = "";
		long v=0;
		try { // Number format exception
		long r=Long.parseLong(indexR);
		
		long d=0;
		long x=0;
	//Error 1: if user puts anything apart from the valid assembly instruction operations(LI,LW,AW,STW)
	if(operation.equalsIgnoreCase("LI")||operation.equalsIgnoreCase("LW")||operation.equalsIgnoreCase("AW")||operation.equalsIgnoreCase("STW")) {
		
		 if (operation.equalsIgnoreCase("LI")) {
			machine = machine | (0b0100010 << 24);// left shifting 24 to move LI to the first 8 bits
			if(0<=r&&r<=15) {
				machine = machine |(r << 20);}
				else {
					errorLabel.setText("ERROR - Illeagl value for R");
					binaryInstruction.setText("");
					hexInstruction.setText("");
					return;
				}
			indexV = assemblyInput.substring(spaceIndex+1).trim();
			v = Long.parseLong(indexV);
			if(-524288<=v&&v<=524287) {
			v = v & (0xFFFFF); //for negative values
			machine = machine | (v);
			}
			else {
				errorLabel.setText("ERROR - Illeagl value in LI");
				binaryInstruction.setText("");
				hexInstruction.setText("");
				return;
			}
			
			}
		 
		
		 else if (operation.equalsIgnoreCase("LW")) {
			
			if (asteriskIndex>0) { //IF THERE IS AN ASTERISK
				if (commaInput==secondComma) { //if there is no second comma 
					machine = machine |(1 << 31);
					machine = machine | (0b0110010 << 24);
					if(0<=r&&r<=15) {
					machine = machine |(r << 20);}
					else {
						errorLabel.setText("ERROR - Illeagl value for R");
						binaryInstruction.setText("");
						hexInstruction.setText("");
						return;
					}
					indexD = assemblyInput.substring(asteriskIndex+1).trim();
					d = Long.parseLong(indexD);
					if(0<=d&&d<=131071) {
					machine = machine |(d);	}
					else {
						errorLabel.setText("ERROR - Illeagl value for D");
						binaryInstruction.setText("");
						hexInstruction.setText("");
						return;
					}
					
				}
				else { //IF THERE IS SECOND COMMA
					machine = machine |(1 << 31);
					machine = machine | (0b0110010 << 24);
					if(0<=r&&r<=15) {
						machine = machine |(r << 20);}
						else {
							errorLabel.setText("ERROR - Illeagl value for R");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexX = assemblyInput.substring(secondComma+1);
					x = Long.parseLong(indexX);
					if(1<=x&&x<=7) {
						machine =machine | (x << 17);}
						else {
							errorLabel.setText("ERROR - Illeagl value for X");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					
					indexD = assemblyInput.substring(asteriskIndex+1,secondComma).trim();
					d = Long.parseLong(indexD);
					if(0<=d&&d<=131071) {
						machine = machine |(d);	}
						else {
							errorLabel.setText("ERROR - Illeagl value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					
					
				     }
				
				}
			else { //CHECK IF THERE IS NO ASTERISK
				if (commaInput==secondComma) { // THERE IS NO SECOND COMMA 
					machine = machine | (0<<31); //this has no effect
					machine = machine | (0b0110000 << 24);
					if(0<=r&&r<=15) {
						machine = machine |(r << 20);}
						else {
							errorLabel.setText("ERROR - Illeagl value for R");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexD = assemblyInput.substring(spaceIndex+1).trim();	
					d = Long.parseLong(indexD);
					if(0<=d&&d<=131071) {
						machine = machine |(d);	}
						else {
							errorLabel.setText("ERROR - Illeagl value in for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					
				}
				else { //IF THERE IS A SECOND COMMA 
					machine = machine | (0b0110000 << 24);
					if(0<=r&&r<=15) {
						machine = machine |(r << 20);}
						else {
							errorLabel.setText("ERROR - Illeagl value for R");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexX = assemblyInput.substring(secondComma+1);
					x = Long.parseLong(indexX);
					if(1<=x&&x<=7) {
						machine =machine | (x << 17);}
						else {
							errorLabel.setText("ERROR - Illeagl value for X");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexD = assemblyInput.substring(spaceIndex+1,secondComma).trim();
					d = Long.parseLong(indexD);
					if(0<=d&&d<=131071) {
						machine = machine |(d);	}
						else {
							errorLabel.setText("ERROR - Illeagl value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
				}
				
			}
			
		}
		else if(operation.equalsIgnoreCase("AW")){
			if (asteriskIndex>0) { //IF THERE IS AN ASTERISK
				if (commaInput==secondComma) {//CHECK IF THERE IS A SECOND COMMA
	
					machine = machine |(1 << 31);
					machine = machine | (0b0110000 << 24);
					
					if(0<=r&&r<=15) {
						machine = machine |(r << 20);}
						else {
							errorLabel.setText("ERROR - Illeagl value for R");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexD = assemblyInput.substring(asteriskIndex+1).trim();
					d = Long.parseLong(indexD);
					if(0<=d&&d<=131071) {
						machine = machine |(d);	}
						else {
							errorLabel.setText("ERROR - Illeagl value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
				}
				else { //IF THERE IS NO ASTERISK
					machine = machine |(1 << 31);
					machine = machine | (0b0110000 << 24);
					if(0<=r&&r<=15) {
						machine = machine |(r << 20);}
						else {
							errorLabel.setText("ERROR - Illeagl value for R");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexX = assemblyInput.substring(secondComma+1);
					x = Long.parseLong(indexX);
					if(1<=x&&x<=7) {
						machine =machine | (x << 17);}
						else {
							errorLabel.setText("ERROR - Illeagl value for X");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexD = assemblyInput.substring(asteriskIndex+1,secondComma).trim();
					d = Long.parseLong(indexD);
					if(0<=d&&d<=131071) {
						machine = machine |(d);	}
						else {
							errorLabel.setText("ERROR - Illeagl value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
				     }
				}
			else { //IF THERE IS NO ASTERISK
				if (commaInput==secondComma) { //IF THERE IS NO SECOND COMMA 
					machine = machine | (0<<31); //this has no effect
					machine = machine | (0b0110000 << 24);
					if(0<=r&&r<=15) {
						machine = machine |(r << 20);}
						else {
							errorLabel.setText("ERROR - Illeagl value for R");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexD = assemblyInput.substring(spaceIndex+1).trim();	
					d = Long.parseLong(indexD);
					if(0<=d&&d<=131071) {
						machine = machine |(d);	}
						else {
							errorLabel.setText("ERROR - Illeagl value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
				}
				else { //IF THERE IS A SECOND COMMA 
					machine = machine | (0b0110000 << 24);
					if(0<=r&&r<=15) {
						machine = machine |(r << 20);}
						else {
							errorLabel.setText("ERROR - Illeagl value for R");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexX = assemblyInput.substring(secondComma+1);
					x = Long.parseLong(indexX);
					if(1<=x&&x<=7) {
						machine =machine | (x << 17);}
						else {
							errorLabel.setText("ERROR - Illeagl value for X");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexD = assemblyInput.substring(spaceIndex+1,secondComma).trim();
					d = Long.parseLong(indexD);
					if(0<=d&&d<=131071) {
						machine = machine |(d);	}
						else {
							errorLabel.setText("ERROR - Illeagl value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
				}
			}
		
		}
		else if (operation.equalsIgnoreCase("STW")) {
			if (asteriskIndex>0) { //IF THERE IS AN ASTERISK
				if (commaInput==secondComma) { //IF THERE IS NO SECOND COMMA 
					machine = machine |(1 << 31);
					machine = machine | (0b0110101 << 24);
					if(0<=r&&r<=15) {
						machine = machine |(r << 20);}
						else {
							errorLabel.setText("ERROR - Illeagl value for R");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexD = assemblyInput.substring(asteriskIndex+1).trim();
					d = Long.parseLong(indexD);
					if(0<=d&&d<=131071) {
						machine = machine |(d);	}
						else {
							errorLabel.setText("ERROR - Illeagl value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
				}
				else { //IF THERE IS A SECOND COMMA
					machine = machine |(1 << 31);
					machine = machine | (0b0110101 << 24);
					if(0<=r&&r<=15) {
						machine = machine |(r << 20);}
						else {
							errorLabel.setText("ERROR - Illeagl value for R");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexX = assemblyInput.substring(secondComma+1);
					x = Long.parseLong(indexX);
					x = x & (0xFFFFF);
					if(1<=x&&x<=7) {
						machine =machine | (x << 17);}
						else {
							errorLabel.setText("ERROR - Illeagl value for X");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexD = assemblyInput.substring(asteriskIndex+1,secondComma).trim();
					d = Long.parseLong(indexD);
					d = d & (0xFFFFF);
					if(0<=d&&d<=131071) {
						machine = machine |(d);	}
						else {
							errorLabel.setText("ERROR - Illeagl value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
				     }
				}
			else { //WHEN THERE IS NO ASTERISK
				if (commaInput==secondComma) { //IF THERE IS NO COMMA THEN THE LAST COMMA WOULD BE THE FIRST COMMA
					machine = machine | (0<<31); 
					machine = machine | (0b0110101 << 24);
					if(0<=r&&r<=15) {
						machine = machine |(r << 20);}
						else {
							errorLabel.setText("ERROR - Illeagl value for R");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexD = assemblyInput.substring(spaceIndex+1).trim();	
					d = Long.parseLong(indexD);
					d = d & (0xFFFFF);
					if(0<=d&&d<=131071) {
						machine = machine |(d);	}
						else {
							errorLabel.setText("ERROR - Illeagl value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
				}
				else { //IF THERE IS A SECOND COMMA 					
					machine = machine | (0b0110101 << 24);
					if(0<=r&&r<=15) {
						machine = machine |(r << 20);}
						else {
							errorLabel.setText("ERROR - Illeagl value for R");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexX = assemblyInput.substring(secondComma+1);
					x = Long.parseLong(indexX);
					x = x & (0xFFFFF);
					if(1<=x&&x<=7) {
						machine =machine | (x << 17);}
						else {
							errorLabel.setText("ERROR - Illeagl value for X");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
					indexD = assemblyInput.substring(spaceIndex+1,secondComma).trim();
					d = Long.parseLong(indexD);
					d = d & (0xFFFFF);
					if(0<=d&&d<=131071) {
						machine = machine |(d);	}
						else {
							errorLabel.setText("ERROR - Illeagl value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
							return;
						}
				}
			}
			
		}
	
		 // results
		int machineInt = (int)(machine);//cast
		String bi =	displayIntAsBinary(machineInt);
		String hex = displayIntAsHex(machineInt);
		binaryInstruction.setText(bi);
		hexInstruction.setText(hex);
		
		}
	
	else { // ERRORS
			errorLabel.setText("ERROR - Invalid mnemonic ");
			binaryInstruction.setText("");
			hexInstruction.setText("");
		
		}}
		catch(NumberFormatException e){
			errorLabel.setText("ERROR - Illegal Number Format");
			binaryInstruction.setText("");
			hexInstruction.setText("");
		}}
		catch(StringIndexOutOfBoundsException e) {
			errorLabel.setText("ERROR - Invalid first part");
			binaryInstruction.setText("");
			hexInstruction.setText("");
		}
	}
		
	

	void decodeBin() {
		
		String assemblyInput = binaryInstruction.getText().trim(); 
		if (assemblyInput.length()==32) {// 32 DIGITS ONLY 
			if(!assemblyInput.matches("[0-1]+")) {//Illegal values
				errorLabel.setText("ERROR - Illegal value for Binary.");
				hexInstruction.setText("");
				assemblerInstruction.setText("");
			}
		    else {
		String assembly = "";
		int binInt = (int)(Long.parseLong(assemblyInput,2));
		int firstEight = (binInt>>24) & (0b1111111);
		int r = (binInt>>20)&(0b1111);
		int v = (binInt) & (0xFFFFF);
		v= (binInt<<12)>>12; //why??
		int x = (binInt>>17)&(0b111);
		int d = (binInt)&(0b11111111111111111);
		
		if (firstEight==0b0100010) {
				assembly = "LI"+","+r+" "+v;
				} 
//			else {
//				errorLabel.setText("ERROR - * cannot be used with LI");
//				assemblerInstruction.setText("");
//				return;
//			}
			
//		else if (firstEight==0b10100010) {
//			errorLabel.setText("ERROR - * cannot be used with LI");
//			assemblerInstruction.setText("");
//			hexInstruction.setText("");
//			
//		}
	
		else if(firstEight==0b0110010) {
			if (binInt>0) {// there is a 0 in the higher order bit --> there is NO asterisk 
				if (x>0) { // there is x ---> there is a second comma
				assembly = "LW" + "," + r + " " + d+ "," +x;
				}
				else{// there is no x ---> there is no second comma
				assembly = "LW"+","+r+" "+d;	
				}
			}
			else {// THERE IS A ASTERISK
				if (x>0) { // there is x ---> there is a second comma
					assembly = "LW" + "," + r + " " +"*"+ d+ "," +x;
					}
				else{// there is no x ---> there is no second comma
					assembly = "LW"+","+r+" "+"*"+d;	
					}
			}
			
		}
		else if(firstEight==0b0110000) {
			if (binInt>0) {// there is a 0 in the higher order bit --> there is NO asterisk 
				if (x>0) { // there is x ---> there is a second comma
				assembly = "AW" + "," + r + " " + d+ "," +x;
				}
				else{// there is no x ---> there is no second comma
				assembly = "AW"+","+r+" "+d;	
				}
			}
			else {// THERE IS A ASTERISK
				if (x>0) { // there is x ---> there is a second comma
					assembly = "AW" + "," + r + " " +"*"+ d+ "," +x;
					}
				else{// there is no x ---> there is no second comma
					assembly = "AW"+","+r+" "+"*"+d;	
					}
			}
			
		}
		else if(firstEight==0b0110101) {
			if (binInt>0) {// there is a 0 in the higher order bit --> there is NO asterisk 
				if (x>0) { // there is x ---> there is a second comma
				assembly = "STW" + "," + r + " " + d+ "," +x;
				}
				else{// there is no x ---> there is no second comma
				assembly = "STW"+","+r+" "+d;	
				}
			}
			else {// THERE IS A ASTERISK
				if (x>0) { // there is x ---> there is a second comma
					assembly = "STW" + "," + r + " " +"*"+ d+ "," +x;
					}
				else{// there is no x ---> there is no second comma
					assembly = "STW"+","+r+" "+"*"+d;	
					}
			}
			
		}
	
		assemblerInstruction.setText(assembly);
	    String hex = displayIntAsHex(binInt);
	    hexInstruction.setText(hex);
	}}
	else {
		errorLabel.setText("ERROR - Binary must be 32 digits.");
		hexInstruction.setText("");
		assemblerInstruction.setText("");
	}
		}

	void decodeHex() {
		String assemblyInput = hexInstruction.getText().trim(); 
		String assembly = "";
		
		if(assemblyInput.length()==8) {// must be 8 digits
		
		if((!assemblyInput.matches("[0-9A-F]+"))&&(!assemblyInput.matches("[0-9a-f]+"))){//illegal values
			errorLabel.setText("ERROR - Illegal value for Hex.");
			binaryInstruction.setText("");
			assemblerInstruction.setText("");
		}
		else {
		int hexInt = (int)(Long.parseLong(assemblyInput,16));
		int firstEight = (hexInt>>24) & (0b1111111);
		int r = (hexInt>>20)&(0b1111);
		int v = (hexInt) & (0xFFFFF);
		v= (hexInt<<12)>>12; //why
		int x = (hexInt>>17)&(0b111);
		int d = (hexInt)&(0b11111111111111111);
		if (firstEight==0b0100010) {
			assembly = "LI"+","+r+" "+v;
			}
			
			else if(firstEight==0b0110010) {
				if (hexInt>0) {// there is a 0 in the higher order bit --> there is NO asterisk 
					if (x>0) { // there is x ---> there is a second comma
					assembly = "LW" + "," + r + " " + d+ "," +x;
					}
					else{// there is no x ---> there is no second comma
					assembly = "LW"+","+r+" "+d;	
					}
				}
				else {// THERE IS A ASTERISK
					if (x>0) { // there is x ---> there is a second comma
						assembly = "LW" + "," + r + " " +"*"+ d+ "," +x;
						}
					else{// there is no x ---> there is no second comma
						assembly = "LW"+","+r+" "+"*"+d;	
						}
				}
				
			}
			else if(firstEight==0b0110000) {
				if (hexInt>0) {// there is a 0 in the higher order bit --> there is NO asterisk 
					if (x>0) { // there is x ---> there is a second comma
					assembly = "AW" + "," + r + " " + d+ "," +x;
					}
					else{// there is no x ---> there is no second comma
					assembly = "AW"+","+r+" "+d;	
					}
				}
				else {// THERE IS A ASTERISK
					if (x>0) { // there is x ---> there is a second comma
						assembly = "AW" + "," + r + " " +"*"+ d+ "," +x;
						}
					else{// there is no x ---> there is no second comma
						assembly = "AW"+","+r+" "+"*"+d;	
						}
				}
				
			}
			else if(firstEight==0b0110101) {
				if (hexInt>0) {// there is a 0 in the higher order bit --> there is NO asterisk 
					if (x>0) { // there is x ---> there is a second comma
					assembly = "STW" + "," + r + " " + d+ "," +x;
					}
					else{// there is no x ---> there is no second comma
					assembly = "STW"+","+r+" "+d;	
					}
				}
				else {// THERE IS A ASTERISK
					if (x>0) { // there is x ---> there is a second comma
						assembly = "STW" + "," + r + " " +"*"+ d+ "," +x;
						}
					else{// there is no x ---> there is no second comma
						assembly = "STW"+","+r+" "+"*"+d;	
						}
				}
				
			}
		
			assemblerInstruction.setText(assembly);
		    String bi = displayIntAsBinary(hexInt);
		    binaryInstruction.setText(bi);
		
	}}
		else {
		errorLabel.setText("ERROR - Hex must be 8 digits.");
		binaryInstruction.setText("");
		assemblerInstruction.setText("");
	}
}
	
	
}
