package karthikCalc;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



public class Calc extends JFrame implements ActionListener {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L; //declares serial version UID. I'm not entirely sure what this does but Eclipse suggested that I use it, otherwise an error message comes up.
	
	//pretty self-explanatory
	double num1; 
	double num2; 
	double result; 
	
	
	JButton[] button = new JButton[20]; //creates an array of 20 button objects
	String[] buttonString = {"7", "8", "9", "+", 
							 "4", "5", "6", "-",
							 "1", "2", "3", "*",
							 "0", ".", "<-", "/",
							 "CE", "C", "=", "^"}; //this is an array of the text that I want to put in each of my buttons
	JPanel grid = new JPanel(new GridLayout(5,5,5,5)); //grid for the buttons
	JPanel dis = new JPanel(new GridLayout(3,1)); //grid for the output
	JTextField display = new JTextField(); //text field for the display
	JLabel runningTotal = new JLabel("Running total: " + Double.toString(result)); //label to show the running total if you do more than 1 operation in one calculation
	JLabel lastOp = new JLabel("Current operation: "); //label to show the operation you pressed in case you forget
	
	//fonts for fun
	Font numberFont = new Font("SansSerif", Font.PLAIN, 20); 
	Font operatorFont = new Font("SansSerif", Font.BOLD, 20);
	

	Boolean displayingResult = false; //boolean for whether the display is showing a result or showing input
	Boolean dotPressed = false; //boolean for whether or not a decimal point has been entered so that you don't enter a number with 2 or more decimal point
	int op; //stores number which is assigned to an operation; e.g. 0 = addition.
	
	
	//constructor
	public Calc() {
		
		super("Calculator by Karthik"); //sets the title
		setSize(300,480); //set the size of frame
		setDefaultCloseOperation(EXIT_ON_CLOSE); //so that it exits when you press the close button
		setVisible(true); //so that it is visible
		setResizable(false); //so that the window cannot be resized (otherwise it looks ugly)
		
		Container contentPane = getContentPane(); //creates a container and a content pane
		
		//formatting
		contentPane.add("Center", grid);
		contentPane.add("North", dis);
		
		//adding the components onto the grids
		dis.add(runningTotal);
		dis.add(lastOp);
		dis.add(display);
		
		//more formatting
		display.setHorizontalAlignment(JTextField.RIGHT);
		display.setFont(numberFont);
		display.setEditable(false);
		runningTotal.setHorizontalAlignment(JLabel.RIGHT);
		lastOp.setHorizontalAlignment(JLabel.RIGHT);
		
		
		//sets font for each button and adds each button onto grid; adds action listener for when th button is clicked
		for(int i = 0; i < button.length; i++)
		{
			button[i] = new JButton(buttonString[i]);
			
			if(i == 3 || i == 7 || i == 11 || i == 15 || i == 19)
			{
				button[i].setFont(operatorFont);
				button[i].setBackground(Color.YELLOW);
			}
			else if(i == 14 || i == 16 || i == 17 || i == 18)
			{
				button[i].setFont(operatorFont);
				button[i].setBackground(Color.GREEN);
			}
			else
			{
				button[i].setFont(numberFont);
			}
			button[i].addActionListener(this);
			grid.add(button[i]);
			
			
		}
		
				
	}

	//this is carried out when a button is pressed
	public void actionPerformed(ActionEvent e)
	{
		
		//if a number button is a pressed
		if(e.getSource() != button[3] && e.getSource() != button[7] && e.getSource() != button[11] && 
		   e.getSource() != button[13] && e.getSource() != button[14] && e.getSource() != button[15] && 
		   e.getSource() != button[16] && e.getSource() != button[17] && e.getSource() != button[18] && 
		   e.getSource() != button[19])
		{
			if(displayingResult)
			{
				display.setText(e.getActionCommand());
				displayingResult = false;
			}
			else
			{
				display.setText(display.getText() + e.getActionCommand());
			}
		}
		
		
		
		//if the decimal point button is pressed and has not been pressed already
		if(!dotPressed && e.getSource() == button[13])
		{
			if(displayingResult)
			{
				display.setText(e.getActionCommand());
				displayingResult = false;
				dotPressed = true;
			}
			else
			{
				display.setText(display.getText() + e.getActionCommand());
				dotPressed = true;
			}
		}
		
		//if the operations buttons are pressed
		if(e.getSource() == button[3])
		{
			add();
		}
		
		if(e.getSource() == button[7])
		{
			sub();
		}
		
		if(e.getSource() == button[11])
		{
			mul();
		}
		
		
		
		if(e.getSource() == button[15])
		{
			div();
		}
		
		if(e.getSource() == button[19])
		{
			power();
		}
		
		
		//if backspace button is pressed
		if(e.getSource() == button[14])
		{
			backspace();
		}
		
		//if equals button is pressed
		if(e.getSource() == button[18])
		{
			eq();
		}
		
		//if CE is pressed
		if(e.getSource() == button[16])
		{
			clearEntry();
		}
		
		//if C is pressed
		if(e.getSource() == button[17])
		{
			clear();
		}
	}
	
	
	//the methods for operations
	//add
	public void add()
	{
		check();
		recordOperation(0, "+");	
	}
	//subtract
	public void sub()
	{
		
		//in case you want to type a negative sign instead of carrying out a subtraction
		if(display.getText().length() == 0)
		{
			display.setText(display.getText() + "-");
			displayingResult = false;
		}
		
		else
		{
			check();
			recordOperation(1, "-");

		}
			
	}
	
	//multiply
	public void mul()
	{
		check();
		recordOperation(2, "*");
	}
	
	//division
	public void div()
	{
		check();
		recordOperation(3, "/");
	}
	
	//raise to the power of
	public void power()
	{
		check();
		recordOperation(4, "^");

	}
	
	//records operation and waits for num2
	public void recordOperation(int a, String b)
	{
		num1 = Double.parseDouble(display.getText());
		display.setText("");
		lastOp.setText("Current operation: " + b);
		op = a;
		dotPressed = false; //so that you can type a decimal point in the second number
	}
	
	
	//actually carries out the operation
	public void operation()
	{
		switch(op)
		{
		case 0 : result = num1 + num2; break;
		case 1 : result = num1 - num2; break;
		case 2 : result = num1 * num2; break;
		case 3 : result = num1 / num2; break;
		case 4 : result = Math.pow(num1, num2); break;
		}
	}
	
	
	//checks if there is a number in the running total that is not 0
	public void check()
	{
		if(num1 != 0)
		{
			num2 = Double.parseDouble(display.getText());
			operation();
			display.setText(Double.toString(result));
			runningTotal.setText("Running total: " + Double.toString(result));
		}
	}
	
	
	//equals
	public void eq()
	{
		num2 = Double.parseDouble(display.getText());
		operation();
		
		//resets the numbers stored to 0 for the next operation
		num1 = 0;
		num2 = 0;
		
		display.setText(Double.toString(result));
		runningTotal.setText("Running total: " + Double.toString(result));
		lastOp.setText("Current operation: None");
		displayingResult = true;
		dotPressed = false;

		
	}
	
	
	//clears the display
	public void clearEntry()
	{
		display.setText("");
		dotPressed = false;

	}
	
	
	//clears everything including running total and the numbers stored
	public void clear()
	{
		display.setText("");
		num1 = 0;
		num2 = 0;
		result = 0;
		runningTotal.setText("Running total: " + "0.0");
		lastOp.setText("Current operation: None");
		dotPressed = false;


	}
	
	
	//deletes last character in display
	public void backspace()
	{
		
		if(display.getText().length() > 0)
		{
			if(display.getText().charAt(display.getText().length() - 1) == '.')
			{
				dotPressed = false;
			}
			display.setText(display.getText().substring(0, display.getText().length() - 1));
		}
	}
	
	
	
	//main method
	public static void main(String[] args)
	{
		new Calc();
	}
	
}

	