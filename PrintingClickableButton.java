import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class PrintingClickableButton here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PrintingClickableButton extends ClickableButton
{
    public void press() {
        super.press();
        System.out.println("Hi!");
    }
    
    public void release() {
        super.release();
        System.out.println("Bye");
    }
}
