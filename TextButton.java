import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Generic Button to display text that is clickable.
 * This button can be added to and controlled by a world.
 * 
 * @author Mandy Yuan
 * @version Oct 20, 2024
 */
public class TextButton extends Actor {

    // Declare instance variables for the button's images and text
    private GreenfootImage myImage;     // Image representing the button in its default state
    private GreenfootImage myAltImage;  // Image representing the button in its alternative (hovered/clicked) state
    private String buttonText;           // The text displayed on the button
    private int textSize;                // Size of the text to be displayed on the button

    /**
     * Construct a TextButton given only a String.
     * This constructor sets a default text size of 20.
     * 
     * @param text The String to be displayed on the button
     */
    public TextButton(String text) {
        // Call the second constructor with default text size of 20
        this(text, 20);
    }

    /**
     * Construct a TextButton given a String and a text size.
     * 
     * @param text The String to be displayed on the button
     * @param textSize The size of the text to be displayed on the button
     */
    public TextButton(String text, int textSize) {
        // Assign the provided text to the buttonText variable
        buttonText = text;
        // Assign the provided text size to the instance variable
        this.textSize = textSize;
        // Draw the button with centered text by calling the updateMe method
        updateMe(text);
    }

    public void act() {
        // Check if the mouse is pressed on this TextButton
        if (Greenfoot.mousePressed(this)) {
            // If the button is clicked, set the image to the alternative image (myAltImage)
            setImage(myAltImage);
        } else {
            // If the button is not clicked, reset the image to the default image (myImage)
            setImage(myImage);
        }
    }

    /**
     * Update the current TextButton text and its appearance.
     * This method modifies the button's text and redraws both the normal and 
     * alternative button images.
     * 
     * @param text The new text to be displayed on the button
     */
    public void updateMe(String text) {
        // Update the buttonText variable with the new text
        buttonText = text;

        // Create a temporary image for the normal state of the button with red text on a white background
        GreenfootImage tempTextImage = new GreenfootImage(text, textSize, Color.RED, Color.WHITE);

        // Create a new image for the button that is slightly larger than the text
        myImage = new GreenfootImage(tempTextImage.getWidth() + 8, tempTextImage.getHeight() + 8);
        myImage.setColor(Color.WHITE); // Set the background color to white
        myImage.fill(); // Fill the background
        myImage.drawImage(tempTextImage, 4, 4); // Draw the text image onto the button

        // Set the border color to black and draw a rectangle around the button
        myImage.setColor(Color.BLACK);
        myImage.drawRect(0, 0, tempTextImage.getWidth() + 7, tempTextImage.getHeight() + 7);
        setImage(myImage); // Set the current image of this actor to myImage

        // Create a temporary image for the alternative state of the button with white text on a red background
        tempTextImage = new GreenfootImage(text, textSize, Color.WHITE, Color.RED);
        myAltImage = new GreenfootImage(tempTextImage.getWidth() + 8, tempTextImage.getHeight() + 8);
        myAltImage.setColor(Color.WHITE); // Set the background color to white
        myAltImage.fill(); // Fill the background
        myAltImage.drawImage(tempTextImage, 4, 4); // Draw the text image onto the button

        // Set the border color to black and draw a rectangle around the button
        myAltImage.setColor(Color.BLACK);
        myAltImage.drawRect(0, 0, tempTextImage.getWidth() + 7, tempTextImage.getHeight() + 7);
    }

}
