package com.adamratana.timekeeperevolution.engine.utils;

import java.util.Calendar;

public class Planets {
	//Script from http://mysite.verizon.net/res148h4j/javascript/script_planet_orbits.html
	private static final double  DEGS = 180/Math.PI;                  // convert radians to degrees
	private static final double RADS = Math.PI/180;                  // convert degrees to radians
	private static final double EPS  = 1.0e-12;                      // machine error constant

	private final String[] pname = new String[]{"Mercury", "Venus  ", "Earth  ",
			"Mars   ", "Jupiter", "Saturn ",
			"Uranus ", "Neptune", "Pluto  "};

	public static double getTrueAnomaly(int planet, Calendar date)
	{
		Vector2d c = getCoord(planet, dayNumber(date));
		return triAngle(c.x, c.y);
	}

	static double triAngle(double x, double y){
		if(x == 0)
			return (y>0) ? 180 : 0;

		double a = Math.atan(y/x)*180/Math.PI;
		a = (x > 0) ? a+90 : a+270;

		return Math.round(a);
	}

	// convert angle (deg, min, sec) to degrees as real
	static double dms2real(double deg, double min, double sec )
	{
		if (deg < 0)
			return deg - min/60 - sec/3600;
		else
			return deg + min/60 + sec/3600;
	}
	// day number to/from J2000 (Jan 1.5, 2000)
	private static double dayNumber(Calendar now)
	{
		double y    = now.get(Calendar.YEAR);
		double m    = now.get(Calendar.MONTH) + 1;
		double d    = now.get(Calendar.DAY_OF_MONTH);
		double hour = now.get(Calendar.HOUR);
		double mins = now.get(Calendar.MINUTE);

		double h = hour + mins/60;
		return 367*y
				- Math.floor(7*(y + Math.floor((m + 9)/12))/4)
				+ Math.floor(275*m/9) + d - 730531.5 + h/24;
	}

	// compute RA, DEC, and distance of planet-p for day number-d
// result returned in structure obj in degrees and astronomical units
	private static Vector2d getCoord(int i, double d)
	{
		// orbital element structure
		Planet p = new Planet();

		meanElements(p, i, d);

		// position of planet in its orbit
		double mp = mod2pi(p.L - p.w);
		double vp = true_anomaly(mp, p.e);
		double rp = p.a*(1 - p.e*p.e)/(1 + p.e*Math.cos(vp));

		// heliocentric rectangular coordinates of planet
		double xh = rp*(Math.cos(p.O)*Math.cos(vp + p.w - p.O) - Math.sin(p.O)*Math.sin(vp + p.w - p.O)*Math.cos(p.i));
		double yh = rp*(Math.sin(p.O)*Math.cos(vp + p.w - p.O) + Math.cos(p.O)*Math.sin(vp + p.w - p.O)*Math.cos(p.i));
		// var zh = rp*(Math.sin(vp + p.w - p.O)*Math.sin(p.i));

		// return { mp:mp, vp:vp, rp:rp,  xh:xh, yh:yh, zh:zh }
		return new Vector2d(xh, yh);
	}

	// Compute the elements of the orbit for planet-i at day number-d
// result is returned in structure p
	private static void meanElements(Planet p, int i, double d)
	{
		double cy = d/36525;                    // centuries since J2000

		switch (i)
		{
			case 0: // Mercury
				p.a = 0.38709893 + 0.00000066*cy;
				p.e = 0.20563069 + 0.00002527*cy;
				p.i = ( 7.00487  -  23.51*cy/3600)*RADS;
				p.O = (48.33167  - 446.30*cy/3600)*RADS;
				p.w = (77.45645  + 573.57*cy/3600)*RADS;
				p.L = mod2pi((252.25084 + 538101628.29*cy/3600)*RADS);
				break;
			case 1: // Venus
				p.a = 0.72333199 + 0.00000092*cy;
				p.e = 0.00677323 - 0.00004938*cy;
				p.i = (  3.39471 -   2.86*cy/3600)*RADS;
				p.O = ( 76.68069 - 996.89*cy/3600)*RADS;
				p.w = (131.53298 - 108.80*cy/3600)*RADS;
				p.L = mod2pi((181.97973 + 210664136.06*cy/3600)*RADS);
				break;
			case 2: // Earth/Sun
				p.a = 1.00000011 - 0.00000005*cy;
				p.e = 0.01671022 - 0.00003804*cy;
				p.i = (  0.00005 -    46.94*cy/3600)*RADS;
				p.O = (-11.26064 - 18228.25*cy/3600)*RADS;
				p.w = (102.94719 +  1198.28*cy/3600)*RADS;
				p.L = mod2pi((100.46435 + 129597740.63*cy/3600)*RADS);
				break;
			case 3: // Mars
				p.a = 1.52366231 - 0.00007221*cy;
				p.e = 0.09341233 + 0.00011902*cy;
				p.i = (  1.85061 -   25.47*cy/3600)*RADS;
				p.O = ( 49.57854 - 1020.19*cy/3600)*RADS;
				p.w = (336.04084 + 1560.78*cy/3600)*RADS;
				p.L = mod2pi((355.45332 + 68905103.78*cy/3600)*RADS);
				break;
			case 4: // Jupiter
				p.a = 5.20336301 + 0.00060737*cy;
				p.e = 0.04839266 - 0.00012880*cy;
				p.i = (  1.30530 -    4.15*cy/3600)*RADS;
				p.O = (100.55615 + 1217.17*cy/3600)*RADS;
				p.w = ( 14.75385 +  839.93*cy/3600)*RADS;
				p.L = mod2pi((34.40438 + 10925078.35*cy/3600)*RADS);
				break;
			case 5: // Saturn
				p.a = 9.53707032 - 0.00301530*cy;
				p.e = 0.05415060 - 0.00036762*cy;
				p.i = (  2.48446 +    6.11*cy/3600)*RADS;
				p.O = (113.71504 - 1591.05*cy/3600)*RADS;
				p.w = ( 92.43194 - 1948.89*cy/3600)*RADS;
				p.L = mod2pi((49.94432 + 4401052.95*cy/3600)*RADS);
				break;
			case 6: // Uranus
				p.a = 19.19126393 + 0.00152025*cy;
				p.e =  0.04716771 - 0.00019150*cy;
				p.i = (  0.76986  -    2.09*cy/3600)*RADS;
				p.O = ( 74.22988  - 1681.40*cy/3600)*RADS;
				p.w = (170.96424  + 1312.56*cy/3600)*RADS;
				p.L = mod2pi((313.23218 + 1542547.79*cy/3600)*RADS);
				break;
			case 7: // Neptune
				p.a = 30.06896348 - 0.00125196*cy;
				p.e =  0.00858587 + 0.00002510*cy;
				p.i = (  1.76917  -   3.64*cy/3600)*RADS;
				p.O = (131.72169  - 151.25*cy/3600)*RADS;
				p.w = ( 44.97135  - 844.43*cy/3600)*RADS;
				p.L = mod2pi((304.88003 + 786449.21*cy/3600)*RADS);
				break;
			case 8: // Pluto
				p.a = 39.48168677 - 0.00076912*cy;
				p.e =  0.24880766 + 0.00006465*cy;
				p.i = ( 17.14175  +  11.07*cy/3600)*RADS;
				p.O = (110.30347  -  37.33*cy/3600)*RADS;
				p.w = (224.06676  - 132.25*cy/3600)*RADS;
				p.L = mod2pi((238.92881 + 522747.90*cy/3600)*RADS);
				break;
			default:
				throw new IndexOutOfBoundsException("The selected planet is out of bound");
		}
	}

	// return an angle in the range 0 to 2pi radians
	private static double mod2pi(double x)
	{
		double b = x/(2*Math.PI);
		double a = (2*Math.PI)*(b - abs_floor(b));
		if (a < 0) a = (2*Math.PI) + a;
		return a;
	}
	// return the integer part of a number
	private static double abs_floor(double x)
	{
		if (x >= 0.0)
			return Math.floor(x);
		else
			return Math.ceil(x);
	}

	// compute the true anomaly from mean anomaly using iteration
//  M - mean anomaly in radians
//  e - orbit eccentricity
	private static double true_anomaly(double M, double e)
	{
		double V;
		double E1;

		// initial approximation of eccentric anomaly
		double E = M + e*Math.sin(M)*(1.0 + e*Math.cos(M));

		do                                   // iterate to improve accuracy
		{
			E1 = E;
			E = E1 - (E1 - e*Math.sin(E1) - M)/(1 - e*Math.cos(E1));
		}
		while (Math.abs( E - E1 ) > EPS);

		// convert eccentric anomaly to true anomaly
		V = 2*Math.atan(Math.sqrt((1 + e)/(1 - e))*Math.tan(0.5*E));

		if (V < 0) V = V + (2*Math.PI);      // modulo 2pi

		return V;
	}
}
