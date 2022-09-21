package D21125383;
import processing.core.PApplet;
import ddf.minim.*;
import ddf.minim.analysis.FFT;



public class Setup extends PApplet {
     // pause/play key
     int mode = 0;
     int cols;
     int rows;
     int scale =20;
     int w = 5000;
     int h = 3000;
     
     final int SX = 1024;
     final int SY = 600;
     private int frameSize = 512;
     private int sampleRate = 44100;

     float y = 0;
     float smoothedY = 0;
     float smoothedAmplitude = 0;
     float off = 0;
     float n4;
     float n6;
     float per = 0;
     float sum =0;
     float average =0;
     float value =0;
     
     //buffer
     float[] lerpedBuffer;
     float[][] land;
     private float[] bands;
     private float[] smoothedBands;

    private Minim minim;
	private AudioPlayer audioPlayer;
	private AudioBuffer audioBuffer;
	private FFT fft;
     //laura
     // for speaker circle thingy
     
 
     // for progress bar
    
     
 
     //end laura

    //stephen
   
    
   
    //end stephen
    public void setup(){
        colorMode(RGB);
        y = height / 2;
        smoothedY = y;
        cols =w/scale;
        rows=h/scale;
        lerpedBuffer = new float[width];
	}

    public void startMinim() 
	{
		minim = new Minim(this);

		fft = new FFT(frameSize, sampleRate);

		bands = new float[(int) log2(frameSize)];
  		smoothedBands = new float[bands.length];

	}

    float log2(float f) {
		return log(f) / log(2.0f);
	}
    

    public void calculateAverageAmplitude()
	{
        for(int i = 0 ; i < audioBuffer.size() ; i ++)
        {
            sum += abs(audioBuffer.get(i));
            lerpedBuffer[i] = lerp(lerpedBuffer[i], audioBuffer.get(i), 0.05f);
        }
        average= sum / (float) getAudioBuffer().size();

        smoothedAmplitude = lerp(smoothedAmplitude, average, 0.01f);
	}

    public void Stephen(float q){
        land =new float [cols][rows];
                float yoff = q;
                for(int y =0; y < rows;y++){
                    float xoff = 0;
                    for(int x =0; x < cols; x++){
                        land[x][y] =map(noise(xoff, yoff),0,1,-50,50) ;
                        xoff +=1;
                    } 
                    yoff+=q;
                }
                
                
                stroke(map(mouseX, 0, width, 100, 225),map(mouseY, 0, height, 100, 225),0);
                noFill(); 
                if(mousePressed){
                    fill(map(mouseY, 0, width, 70, 225),0,map(mouseX, 0, height, 70, 120));
                } 
                pushMatrix();
                translate(width/2, height/2-280);
                sphere(q*1000);
                popMatrix();

                noFill(); 
                if(mousePressed){
                    fill(map(mouseY, 0, width, 70, 120),0,map(mouseX, 0, height, 70, 120));
                } 
                translate(width/2, height/2);
                rotateX(PI/2.2f);
                translate(-w/2, -h/2);
                for(int y =0; y < rows-1;y++){
                    beginShape(TRIANGLE_STRIP);
                    for(int x =0; x < cols; x++){ 
                        vertex(x*scale, y*scale,land[x][y]);
                        vertex(x*scale, (y+1)*scale, land[x][y+1]);
                    }
                    endShape();
                } 
    }
   
    

     void loadingBar(){
        //progress bar
        background(0);
        per = (float) ((per + 0.16) % 100); 
        //System.out.println(per);
        textSize(30);
        fill(255);
        text("Loading ... " + per + " %", SX / 4, (float) (SY / 2.5));
        rect(SX / 4, SY / 2, per * 2, 20, 7); 
        //System.out.println(per);
        
    }

    void speaker(){
         // like a speaker circle thingy
         noCursor();
         smooth();
         background (0);
         frameRate(24);

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
             
             fill(186,85,211); // medium orchid
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
       background(0);
        //map audio buffer size
        float n = map(20, 0, audioBuffer.size(), 0, 255);
        translate(s,g);
        if(mousePressed)
        {
            lights();
        }
        
        ambientLight(100, n, 255);
        
        //rotate planet on x, y , z axis
        rotateY((float) (frameCount/100.0));
        rotateX((float) (frameCount/50.0));
        rotateZ((float) (frameCount/50.0));

        //fill
        fill(100);
        noStroke();

        //draw sphere
        sphere(size*1.5f);

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
            circle(f*1.6f , i, f*1.4f);
            circle(f * 1.8f, i, f* 1.4f);  
        }
    }
   

	public void loadAudio(String filename)
	{
		audioPlayer = minim.loadFile(filename, frameSize);
		audioBuffer = audioPlayer.mix;
	}

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getSX() {
        return SX;
    }

    public int getSY() {
        return SY;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(int frameSize) {
        this.frameSize = frameSize;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSmoothedY() {
        return smoothedY;
    }

    public void setSmoothedY(float smoothedY) {
        this.smoothedY = smoothedY;
    }

    public float getSmoothedAmplitude() {
        return smoothedAmplitude;
    }

    public void setSmoothedAmplitude(float smoothedAmplitude) {
        this.smoothedAmplitude = smoothedAmplitude;
    }

    public float getOff() {
        return off;
    }

    public void setOff(float off) {
        this.off = off;
    }

    public float getN4() {
        return n4;
    }

    public void setN4(float n4) {
        this.n4 = n4;
    }

    public float getN6() {
        return n6;
    }

    public void setN6(float n6) {
        this.n6 = n6;
    }

    public float getPer() {
        return per;
    }

    public void setPer(float per) {
        this.per = per;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    // public float[] getLerpedBuffer() {
    //     return lerpedBuffer;
    // }

    // public void setLerpedBuffer(float[] lerpedBuffer) {
    //     this.lerpedBuffer = lerpedBuffer;
    //}

    public float[][] getLand() {
        return land;
    }

    public void setLand(float[][] land) {
        this.land = land;
    }

    public float[] getBands() {
        return bands;
    }

    public void setBands(float[] bands) {
        this.bands = bands;
    }

    public float[] getSmoothedBands() {
        return smoothedBands;
    }

    public void setSmoothedBands(float[] smoothedBands) {
        this.smoothedBands = smoothedBands;
    }

    public Minim getMinim() {
        return minim;
    }

    public void setMinim(Minim minim) {
        this.minim = minim;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public void setAudioPlayer(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public AudioBuffer getAudioBuffer() {
        return audioBuffer;
    }

    public void setAudioBuffer(AudioBuffer audioBuffer) {
        this.audioBuffer = audioBuffer;
    }

    public FFT getFft() {
        return fft;
    }

    public void setFft(FFT fft) {
        this.fft = fft;
    }

   

    
}


