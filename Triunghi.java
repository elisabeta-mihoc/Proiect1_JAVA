import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class Triunghi extends Applet {
	Point[] cp;
	Point[] pctm;
	Point[] pcti;
	Point[] pctb;
	Point centruCercC;
	
	Image img;
	Graphics imagine;
	
	Button restart;
	Button mediana;
	Button inaltime;
	Button bisectoare;
	
	int nrpct;
	int moveFlag;
	int r;
	
	String[] not={"A","B","C","G","H","I"};
	
	double rCercC;
	
	boolean traseazaMediane;
	boolean traseazaBisectoare;
	boolean traseazaInaltimi;
	boolean mouseDownPct;
	boolean mouseDownCerc;
	boolean mouseUp;
	
	public void init() {
		cp=new Point[3];
		pctm=new Point[3];
		pctb= new Point[3];
		pcti= new Point[3];
		
		img= createImage(3000,3000);
		imagine=img.getGraphics();
		
		restart= new Button("Restart");
		bisectoare=new Button("Bisectoare");
		mediana=new Button("Mediane");
		inaltime=new Button("Inaltimi");
		add(restart);
		add(mediana);
		add(bisectoare);
		add(inaltime);
		
		traseazaMediane=false;
		traseazaBisectoare=false;
		traseazaInaltimi=false;
		mouseDownPct=false;
		mouseDownCerc=false;
		mouseUp=false;
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	
	public void paint(Graphics g) {
		setBackground(Color.white);
		imagine.setColor(Color.black);
		imagine.clearRect(0,0,size().width,size().height);
		
		//desenam pct-le de control
		if(nrpct<3){
			mediana.setEnabled(false);
			bisectoare.setEnabled(false);
			inaltime.setEnabled(false);
		}
		if (nrpct==3){
			mediana.setEnabled(true);
			bisectoare.setEnabled(true);
			inaltime.setEnabled(true);
			double AB=dist(cp[0],cp[1]);
			double BC=dist(cp[1],cp[2]);
			double AC=dist(cp[2],cp[0]);
			double A=sin(BC,AB,AC);
			double B=sin(AC,AB,BC);
			double C=sin(AB,BC,AC);
			centruCercC= cercC(A,B,C);
			System.out.println("Trag de cerc ="+mouseDownCerc);
			System.out.println(" Raza cerc circumscris "+ rCercC);
			
			if(mouseDownCerc){
				cp[0]=calcCoord(cp[0],centruCercC);
				cp[1]=calcCoord(cp[1],centruCercC);
				cp[2]=calcCoord(cp[2],centruCercC);
				r=(int) Math.round(rCercC);
			}
			else{
				r=(int) Math.round(getRaza(AB,BC,AC));
				rCercC=r;
			}
			
			for(int i=0;i<cp.length;i++){
				System.out.println("x="+cp[i].x+" "+" y="+cp[i].y);
			}
			
			imagine.setColor(Color.blue);
			imagine.fillOval(centruCercC.x-3,centruCercC.y-3,6,6);
			imagine.setColor(Color.blue);
			imagine.drawOval(centruCercC.x-r,centruCercC.y-r,2*r,2*r);
		}
		
		if (nrpct==3 && traseazaMediane){
			pctm[0]=mediana(cp[1],cp[2]);
			pctm[1]=mediana(cp[2],cp[0]);
			pctm[2]=mediana(cp[0],cp[1]);
			imagine.setColor(Color.YELLOW);
			imagine.fillOval(pctm[0].x-3,pctm[0].y-3,6,6);
			imagine.fillOval(pctm[1].x-3,pctm[1].y-3,6,6);
			imagine.fillOval(pctm[2].x-3,pctm[2].y-3,6,6);
			imagine.setColor(Color.GRAY);
			imagine.drawLine(cp[0].x,cp[0].y,pctm[0].x,pctm[0].y);
			imagine.drawLine(cp[1].x,cp[1].y,pctm[1].x,pctm[1].y);
			imagine.drawLine(cp[2].x,cp[2].y,pctm[2].x,pctm[2].y);
			Point intersectieMediana=IntersectieMediana(cp[0],cp[1],cp[2]);
			imagine.setColor(Color.red);
			imagine.fillOval(intersectieMediana.x-3,intersectieMediana.y-3,6,6);
			imagine.setColor(Color.black);
			imagine.drawString(not[3],intersectieMediana.x+3,intersectieMediana.y+3);
		}
		
		if(nrpct==3 && traseazaBisectoare){
			pctb[0]=pctb(cp[1],cp[2],cp[0]);
			pctb[1]=pctb(cp[2],cp[0],cp[1]);
			pctb[2]=pctb(cp[1],cp[0],cp[2]);
			imagine.setColor(Color.YELLOW);
			imagine.fillOval(pctb[0].x-3,pctb[0].y-3,6,6);
			imagine.fillOval(pctb[1].x-3,pctb[1].y-3,6,6);
			imagine.fillOval(pctb[2].x-3,pctb[2].y-3,6,6);
			imagine.setColor(Color.GRAY);
			imagine.drawLine(cp[0].x,cp[0].y,pctb[0].x,pctb[0].y);
			imagine.drawLine(cp[1].x,cp[1].y,pctb[1].x,pctb[1].y);
			imagine.drawLine(cp[2].x,cp[2].y,pctb[2].x,pctb[2].y);
			Point intersectieBisectoarea=IntersectieBiesectoarea(cp[0],cp[1],cp[2]);
			imagine.setColor(Color.red);
			imagine.fillOval(intersectieBisectoarea.x-3,intersectieBisectoarea.y-3,6,6);
			imagine.setColor(Color.black);
			imagine.drawString(not[5],intersectieBisectoarea.x+3,intersectieBisectoarea.y+3);
		}
		
		if(nrpct==3 && traseazaInaltimi){
			pcti[0]=pcti(cp[0],cp[1],cp[2]);
			pcti[1]=pcti(cp[1],cp[2],cp[0]);
			pcti[2]=pcti(cp[2],cp[0],cp[1]);
			imagine.setColor(Color.YELLOW);
			imagine.fillOval(pcti[0].x-3,pcti[0].y-3,6,6);
			imagine.fillOval(pcti[1].x-3,pcti[1].y-3,6,6);
			imagine.fillOval(pcti[2].x-3,pcti[2].y-3,6,6);
			imagine.setColor(Color.GRAY);
			imagine.drawLine(cp[0].x,cp[0].y,pcti[0].x,pcti[0].y);
			imagine.drawLine(cp[1].x,cp[1].y,pcti[1].x,pcti[1].y);
			imagine.drawLine(cp[2].x,cp[2].y,pcti[2].x,pcti[2].y);
			Point intersecteazaInaltimea=IntersecteazaInaltimea(cp[0],cp[1],cp[2]);
			imagine.setColor(Color.red);
			imagine.fillOval(intersecteazaInaltimea.x-3,intersecteazaInaltimea.y-3,6,6);
			imagine.setColor(Color.black);
			imagine.drawString(not[4],intersecteazaInaltimea.x+3,intersecteazaInaltimea.y+3);
		}
		
		if(cp.length>0){
			if(nrpct>1){
				for(int i=0;i<nrpct;i++){
					imagine.setColor(Color.black);
					if(i+1==nrpct){
						imagine.drawLine(cp[i].x,cp[i].y,cp[0].x,cp[0].y);
					}
					else{
						imagine.drawLine(cp[i].x,cp[i].y,cp[i+1].x,cp[i+1].y);
					}
				}
			}
			for(int i=0;i<nrpct;i++){
				imagine.setColor(Color.magenta);
				imagine.fillOval(cp[i].x-3,cp[i].y-3,6,6);
				imagine.setColor(Color.black);
				imagine.drawString(not[i],cp[i].x+3,cp[i].y+3);
			}
		}
		g.drawImage(img,0,0,this);
	}
	
	public boolean action(Event e, Object o){
		if(((Button) e.target).getLabel()=="Restart"){
			cp=new Point[3];
			nrpct=0;
			traseazaMediane=false;
			traseazaBisectoare=false;
			traseazaInaltimi=false;
			mouseDownPct=false;
			mouseDownCerc=false;
			repaint();
			((Button) e.target).setLabel("Restart");
			return true;
		}
		
		if(((Button) e.target).getLabel()=="Mediane" && nrpct==3){
			if(traseazaMediane){
				traseazaMediane=false;
				repaint();
				return true;
			}
			else{
				traseazaMediane=true;
				pctm=new Point[3];
				traseazaBisectoare=false;
				traseazaInaltimi=false;
				repaint();
				return true;
			}
		}
		
		if(((Button) e.target).getLabel()=="Bisectoare"&&nrpct==3){
			if(traseazaBisectoare){
				traseazaBisectoare=false;
				repaint();
				return true;
			}
			else{
				traseazaBisectoare=true;
				pctb=new Point[3];
				traseazaInaltimi=false;
				traseazaMediane=false;
				repaint();
				return true;
			}
		}
		
		if(((Button) e.target).getLabel()=="Inaltimi"&&nrpct==3){
			if(traseazaInaltimi){
				traseazaInaltimi=false;
				repaint();
				return true;
			}
			else{
				traseazaInaltimi=true;
				pcti=new Point[3];
				traseazaBisectoare=false;
				traseazaMediane=false;
				repaint();
				return true;
			}
		}
		
		return false;
	}
	
	public boolean mouseDown(Event evt, int x,int y){
		Point point=new Point(x,y);
		if(nrpct!=3){
			cp[nrpct]=point;
			nrpct++;
		}
		else{
			for(int i=0;i<cp.length;i++){
				for(int j=-8;j<9;j++)
					for(int k=-8;k<9;k++)
						if(point.equals(new Point(cp[i].x+j,cp[i].y+k))){
							moveFlag=i;
							mouseDownPct=true;
						}
			}
			if(!mouseDownPct){
				for(int j=-8;j<9;j++){
					for(int k=-8;k<9;k++){
						if(Math.round(rCercC)==Math.round(dist(centruCercC,new Point(x+j,y+k)))){
							mouseDownCerc=true;
						}
					}
				}
			}
		}
		repaint();
		return true;
	}
	
	public boolean mouseDrag(Event eve, int x, int y){
		if(mouseDownCerc){
			rCercC=(int) Math.round(dist(centruCercC,new Point(x,y)));
		}
		if(mouseDownPct){
			cp[moveFlag].move(x,y);
		}
		repaint();
		return true;
	}
	
	public boolean mouseUp(Event eve, int x, int y){
		mouseDownCerc=false;
		if(mouseDownCerc){
			mouseDownCerc=false;
			mouseDownPct=false;
			mouseUp=true;
		}
		else{
			mouseDownCerc=false;
			mouseDownPct=false;
			mouseUp=true;
		}
			
		return true;
	}
	
	public double dist(Point p1,Point p2){
		double d=0;
		d=Math.sqrt(Math.pow(p1.x-p2.x,2)+Math.pow(p1.y-p2.y,2));
		return d;
	}
	
	public Point cercC(double A, double B, double C){
		Point p=new Point();
		double x=(cp[0].x*A+cp[1].x*B+cp[2].x*C)/(A+B+C);
		double y=(cp[0].y*A+cp[1].y*B+cp[2].y*C)/(A+B+C);
		p.setLocation(x,y);
		return p;
	}
	
	public double sin(double a, double b, double c){
		double s=(Math.pow(b,2)+Math.pow(c,2)-Math.pow(a,2))/(2*b*c);
		s=Math.sin(2*Math.acos(s));
		return s;
	}
	
	public double getRaza(double a,double b,double c){
		double s=(a*b*c)/(Math.sqrt((a+b+c)*(a+b-c)*(a-b+c)*(b+c-a)));
		return s;
	}
	
	public Point mediana(Point v1,Point v2){
		Point p=new Point ();
		p.x=(v1.x+v2.x)/2;
		p.y=(v1.y+v2.y)/2;
		return p;
	}
	
	public double getPanta(Point v1, Point v2){
		double m=(double )(v1.y-v2.y)/(double)(v1.x-v2.x);
		return m;
	}
	
	public double getDistBis(double b, double c, double a){
		double x=0;
		x=(a*b)/(b+c);
		return x;
	}
	
	public Point pctb(Point b, Point c, Point a){
		Point pctb=new Point();
		double panta=getPanta(b,c);
		double lA=dist(b,c);
		double lB=0;
		double lC=0;
		Point pctCurent=new Point();
		if(b.x<=c.x){
			lB=dist(a,b);
			lC=dist(a,c);
			pctCurent=b;
		}
		else{
			lB=dist(a,c);
			lC=dist(a,b);
			pctCurent=c;
		}
		double distanta=getDistBis(lB,lC,lA);
		pctb.x=(int) Math.round(pctCurent.x+distanta*Math.sqrt(1/(1+Math.pow(panta,2))));
		pctb.y=(int) Math.round(pctCurent.y+panta*distanta*Math.sqrt(1/(1+Math.pow(panta,2))));
		return pctb;
	}
	
	public Point pcti(Point a, Point b, Point c){
		Point pcti=new Point();
		if(b.x>c.x){
			Point retinPct=new Point();
			retinPct=b;
			b=c;
			c=retinPct;
		}
		double pantaI=(-1.0)/getPanta(b,c);
		double pantaB=getPanta(b,c);
		pcti.x=(int) Math.round((b.y-a.y+pantaI*a.x-b.x*pantaB)/(pantaI-pantaB));
		pcti.y=(int) Math.round(a.y-pantaI*(a.x-pcti.x));
		return pcti;
	}
	
	public Point IntersectieMediana(Point a, Point b, Point c){
		Point p=new Point();
		p.x=(a.x+b.x+c.x)/3;
		p.y=(a.y+b.y+c.y)/3;
		return p;
	}
	
	public Point IntersectieBiesectoarea(Point a, Point b, Point c){
		Point p=new Point();
		p.x=(int) Math.round(((dist(b,c)*a.x+dist(a,c)*b.x+dist(a,b)*c.x)/(dist(b,c)+dist(a,c)+dist(a,b))));
		p.y=(int) Math.round(((dist(b,c)*a.y+dist(a,c)*b.y+dist(a,b)*c.y)/(dist(b,c)+dist(a,c)+dist(a,b))));
		return p;
	}
	
	public Point IntersecteazaInaltimea(Point a, Point b, Point c){
		Point p=new Point();
		double pAB=getPanta(a,b);
		double pCD=(-1.0)/pAB;
		double pBC=getPanta(b,c);
		double pAE=(-1.0)/pBC;
		p.y=(int)Math.round(((pCD*a.y+pCD*pAE*c.x-pCD*pAE*a.x-pAE*c.y)/(pCD-pAE)));
		p.x=(int)Math.round(((pCD*c.x-c.y+p.y)/(pCD)));
		return p;
	}
	
	public Point calcCoord(Point punct, Point centruC){
		Point p=new Point();
		double panta=getPanta(punct, centruC);
		if(punct.y>=centruC.y){
			p.y=(int) Math.round(rCercC/(Math.sqrt(1/Math.pow(panta,2)+1))+centruC.y);
			p.x=(int) Math.round((p.y-centruC.y)/panta +centruC.x);
		}
		else{
			panta=getPanta(centruC, punct);
			p.y=(int) Math.round(rCercC/(Math.sqrt(1/Math.pow(panta,2)+1))+centruC.y);
			p.x=(int) Math.round((p.y-centruC.y)/-panta+centruC.x);
		}
		return p;
	}
}