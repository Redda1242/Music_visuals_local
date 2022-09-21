package D21125383;
import ddf.minim.AudioBuffer;
import ddf.minim.AudioInput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ie.tudublin.Visual;

public class assignment extends Visual{
    
    //variables
    Minim minim;
    AudioPlayer audioPlayer;
    AudioInput audioInput;
    AudioBuffer audioBuffer;

    // pause/play key
    int mode = 0;//mode refers to visual
    //Raghd
    //buffer
    float[] lerpedBuffer;
    float y = 0;
    float smoothedY = 0;
    float smoothedAmplitude = 0;
    float off = 0;
    //end Raghd

    //laura
    // for speaker thingy
    float n4;
    float n6;
    // for progress bar
    float per = 0;
    final int SX = 1024;
    final int SY = 600;
    //end laura

    //stephen
    int cols;
    int rows;
    int scale =20;
    int w = 5000;
    int h = 3000;
    float[][] land;
    float average =0;
    //end stephen


    //if space key is pressed pause/play
    public void keyPressed() {//start method
		if (key >= '0' && key <= '3') {//if statement 
			mode = key - '0';
		}//end if
		if (keyCode == ' ') {//if statement spacebar
            if (audioPlayer.isPlaying()) {//if audio playing
                audioPlayer.pause();
            }//end if audio playing
            else {//else statement
                audioPlayer.rewind();
                audioPlayer.play();
            }//end else
        }//end if spacebar
	}//end method

    //set to fullscreen with P3D renderer
    public void settings()
    {
        fullScreen(P3D,SPAN);
    }

    public void setup()
    {
        //load music and play
        minim = new Minim(this);
        audioPlayer = minim.loadFile("Heaven.mp3", 1024);
        audioPlayer.play();
        audioBuffer = audioPlayer.mix;
        colorMode(RGB); //set color mode to RGB
        //variables for visual methods
        y = height / 2;
        smoothedY = y;
        cols =w/scale;
        rows=h/scale;
        lerpedBuffer = new float[width];
        //end variables for visual methods
    }
    public void LandScape(float amp){//start Landscape
        stroke(map(mouseX, 0, width, 100, 225),map(mouseY, 0, height, 100, 225),0);//stroke to make outline color
        noFill();//make the shape have no fill
        //mouse press to fill color
        if(mousePressed){//start if 
            fill(map(mouseY, 0, width, 70, 225),0,map(mouseX, 0, height, 70, 120));//fill sphere 
        }//end if 
        //draw sphere
        pushMatrix();
        translate(width/2, height/2-280);//position sphere
        sphere(amp*1000);//make sphere using size and method input
        popMatrix();
        //setup land

        land =new float [cols][rows];//2d array
        float yoff = amp;//make yoff the method input
        for(int y =0; y < rows;y++){//start for loop y
            float xoff = 0;//make xoff =0
            for(int x =0; x < cols; x++){//start for loop x
                land[x][y] =map(noise(xoff, yoff),0,1,-50,50) ;
                xoff +=1;
            }//end for loop x
            yoff+=amp;//yoff plus methos input
        }//end for loop y 

        noFill();//make no fill
        //fill  
        if(mousePressed){//start if
            fill(map(mouseY, 0, width, 70, 120),0,map(mouseX, 0, height, 70, 120));//fill land 
        }//end if 
        translate(width/2, height/2);//start to postion land
        rotateX(PI/2.2f);
        translate(-w/2, -h/2);//end position land
        for(int y =0; y < rows-1;y++){//start for loop to get the number of rows 
            beginShape(TRIANGLE_STRIP);//begins the shape
            for(int x =0; x < cols; x++){ //start for loop to get the number of columns
                vertex(x*scale, y*scale,land[x][y]);//draw vertex
                vertex(x*scale, (y+1)*scale, land[x][y+1]);//draw vertex
            }//end for loop columns
            endShape();//end shape
        }//end forloop  rows
    }//end Landscape
    void loadingBar(){
        //progress bar
        background(0);
        per = (float) ((per + 0.16) % 100); 
        //System.out.println(per);
        textSize(100);
        fill(255);
        text("Loading ... " + per + " %", width/4, (float) (height/2));
        rect(width/4, (height/2)+20, per * 10, 80, 7); 
        //System.out.println(per);
    }
    void speaker(){
        // like a speaker circle thingy
        noCursor();
        smooth();
        background (0);

        fill(0,50);  
        noStroke();
        rect(0, 0, width, height);
        translate(width/2, height/2);
        
        for (int i = 0; i < audioPlayer.bufferSize() - 1; i++) {
        
            float angle = sin(i+n4)* 10; 
            float angle2 = sin(i+n6)* 300; 
            
            float x = sin(radians(i))*(angle2+30); 
            float y = cos(radians(i))*(angle2+30);
            
            float x3 = sin(radians(i))*(500/angle); 
            float y3 = cos(radians(i))*(500/angle);
            
            fill(128,0,128); // purple
            ellipse(x, y, audioPlayer.left.get(i)*10, audioPlayer.left.get(i)*10);
            
            fill(255,255,255); // white
            rect(x3, y3, audioPlayer.left.get(i)*20, audioPlayer.left.get(i)*10);
            
            fill(255, 51,255); // pink
            rect(x, y, audioPlayer.right.get(i)*10, audioPlayer.left.get(i)*10);
            
            fill(255,255,255); // white
            rect(x3, y3, audioPlayer.right.get(i)*10, audioPlayer.right.get(i)*20);
        }  
        n4 += 0.008;
        n6 += 0.04;
   }

    //draw planet method 
    void drawPlanet(float s, float g, float size) 
    { 

        //position of planet
        translate(s, g);
        if(mousePressed)
        {
            lights();
            ambientLight(200, 20, 250);
            directionalLight(255, 60, 126, -1, 0, 0);
        }
        
        ambientLight(100, 20, 100);
        directionalLight(51, 102, 126, -1, 0, 0);
        
        //rotate planet on x, y , z axis
        rotateY((float) (frameCount/100.0));
        rotateX((float) (frameCount/50.0));
        rotateZ((float) (frameCount/50.0));

        //fill
        fill(100);
        noStroke();

        //draw sphere
        sphere(size);

        //loop through buffer and draw the hoops around the planet
        for(int i = 0 ; i < audioBuffer.size()/2 ; i ++)
        {
            
            float c = map(i, 0, audioBuffer.size(), 0, 255);
            float f = lerpedBuffer[i] * height/2 * 4.0f;

            noStroke();
            
            //rotate the hoops around planet
            rotate(c);
            fill(s, f, c);

            //draw hoops
            circle(f*0.6f , i, f*0.4f);
            circle(f * 0.8f, i, f *0.4f);  
        }
    }
    public void draw()
    {//start draw   
        float halfH = height / 2;
        float widthH = width / 2;
        float average = 0;
        float sum = 0;
        int size = 100;
        off += 1;
        // Calculate sum and average of the samples
        // Also lerp each element of buffer;
        for(int i = 0 ; i < audioBuffer.size() ; i ++)//for loop iterate from zero to the buffer size
        {
            sum += abs(audioBuffer.get(i));
            lerpedBuffer[i] = lerp(lerpedBuffer[i], audioBuffer.get(i), 0.05f);
        }
        average= sum / (float) audioBuffer.size();//gets average amplitude
        smoothedAmplitude = lerp(smoothedAmplitude, average, 0.01f);//gets smoothed amplitude
        switch (mode) {//start switch
			case 0:
            {
                //progress bar
                noCursor();
                background(0);
                loadingBar();
                if(per >= 99.6f){
                    mode=1;
                }
                break;
            }
            case 1:
            {
                // like a speaker thingy
                background(0);
                speaker();
                break;  
            }
            case 2:
            {
                noCursor();
                background(0); 
                //print instructions on screen
                textSize((float)(25));	
                fill(200, 140); 
                text("Press Mouse to turn on Lights on this Crazy Planet", CENTER, TOP);
                //draw 3 planets
                drawPlanet(widthH+200, halfH, size);
                drawPlanet(widthH-200, halfH/2, size* 0.3f);
                drawPlanet(widthH-50, halfH, size*0.5f);    
                break;
                
            }

            case 3:
            {//start case
                noCursor();//removesCuror
                background(0);//set background to black
                textSize((float)(25));//sets the size of text	
                fill(200, 140); //fills text
                text("Move Mouse Around To Change Colour And Brightness", CENTER, TOP);//makes and positions text
                text("Press Mouse To Fill, Move Around To Change Colour", CENTER, TOP-35);//makes and positions text
                LandScape(average);//method plus the average amplitude
                break;
            }//end case
        }//end switch
    }//end draw
}//end class