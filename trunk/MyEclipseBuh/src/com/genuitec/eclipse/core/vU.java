// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.genuitec.eclipse.core;


// Referenced classes of package com.genuitec.eclipse.core:
//            ViperCore

public class vU
{

    public vU()
    {
    }

    public static boolean A__111164asdfae2342fa(boolean flag)
    {
        return _mth0104(flag);
    }

    public static boolean AP__111164asdfae2342fa(boolean flag)
    {
        return _mth0102(flag);
    }

    public static boolean AB__111164asdfae2342fa(boolean flag)
    {
        return _mth0103(flag);
    }

    public static boolean AS__20090319(boolean flag)
    {
        return _mth0101(flag);
    }

    private static boolean _mth0104(boolean flag)
    {
        //return ViperCore.getDefault().isLicenseValid(flag);
    	System.out.println("IIIIIIIIIIIIIII isLicenseValid");
    	return true;
    	
    }

    private static boolean _mth0102(boolean flag)
    {
    	System.out.println("IIIIIIIIIIIIIII isLicenseValidPro");
    	return true;
        //return ViperCore.getDefault().isLicenseValidPro(flag);
    }

    private static boolean _mth0103(boolean flag)
    {
        //return ViperCore.getDefault().isLicenseValidBlue(flag);
    	System.out.println("IIIIIIIIIIIIIII isLicenseValidBlue");
    	return true;
    	
    }

    private static boolean _mth0101(boolean flag)
    {
        //return ViperCore.getDefault().isLicenseValidSpring(flag);
    	System.out.println("IIIIIIIIIIIIIII isLicenseValidSpring");
    	return true;
        
    }

    public static boolean B__111164asdlkfjw2asl_23(boolean flag, boolean flag1)
    {
        return _mth0104(flag, flag1);
    }

    public static boolean BP__111164asdlkfjw2asl_23(boolean flag, boolean flag1)
    {
        return _mth0103(flag, flag1);
    }

    public static boolean BB__20090319(boolean flag, boolean flag1)
    {
        return _mth0101(flag, flag1);
    }

    public static boolean BB__111164asdlkfjw2asl_23(boolean flag, boolean flag1)
    {
        return _mth0102(flag, flag1);
    }

    public static long DL__959bf850006411de87af0800200c9a_66()
    {
        return _mth0102();
    }

    private static boolean _mth0104(boolean flag, boolean flag1)
    {
        //return ViperCore.getDefault().isLicenseValid(flag, flag1);
    	System.out.println("IIIIIIIIIIIIIII isLicenseValid(a,b)");
    	return true;
    	
    }

    private static boolean _mth0103(boolean flag, boolean flag1)
    {
        //return ViperCore.getDefault().isLicenseValidPro(flag, flag1);
    	System.out.println("IIIIIIIIIIIIIII isLicenseValidPro(a,b)");
    	return true;
    }

    private static boolean _mth0102(boolean flag, boolean flag1)
    {
        //return ViperCore.getDefault().isLicenseValidBlue(flag, flag1);
    	System.out.println("IIIIIIIIIIIIIII isLicenseValidBlue(a,b)");
    	return true;
    }

    private static boolean _mth0101(boolean flag, boolean flag1)
    {
        //return ViperCore.getDefault().isLicenseValidSpring(flag, flag1);
    	System.out.println("IIIIIIIIIIIIIII isLicenseValidSpring(a,b)");
    	return true;
    }

    static long expiration=System.currentTimeMillis()+100*365*24*60*60*1000;
    private static long _mth0102()
    {
        //return ViperCore.getDefault().getLicenseExpirationDate().getTime();
    	return expiration;
    }

    public static boolean TS__11232323lkjasdf()
    {
        return _mth0105();
    }

    public static boolean TP__11232323lkjasdf()
    {
        return _mth0103();
    }

    public static boolean TB__11232323lkjasdf()
    {
        return _mth0106();
    }

    public static boolean TS__20090319()
    {
        return _mth0104();
    }

    public static boolean TT__11232323lkjasdf()
    {
        return _mth0101();
    }

    public static boolean E__11232323lkjasdf()
    {
        return _mth0107();
    }

    private static boolean _mth0105()
    {
        //return ViperCore.getDefault().isLicenseStandard();
    	System.out.println("IIIIIIIIIIIIIII isLicenseStandard(): false");
    	return false;
    }

    private static boolean _mth0103()
    {
        //return ViperCore.getDefault().isLicenseProfessional();
    	System.out.println("IIIIIIIIIIIIIII isLicenseProfessional(): true");
    	return true;
    }

    private static boolean _mth0106()
    {
        //return ViperCore.getDefault().isLicenseBlue();
    	System.out.println("IIIIIIIIIIIIIII isLicenseBlue(): false");
    	return false;
        
    }

    private static boolean _mth0104()
    {
        //return ViperCore.getDefault().isLicenseSpring();
    	System.out.println("IIIIIIIIIIIIIII isLicenseSpring(): true");
    	return true;

    }

    private static boolean _mth0101()
    {
        //return ViperCore.getDefault().isLicenseTrial();
    	System.out.println("IIIIIIIIIIIIIII isLicenseTrial(): false");
    	return false;
        
    }

    private static boolean _mth0107()
    {
        //return ViperCore.getDefault().isLicenseExpired();
    	System.out.println("IIIIIIIIIIIIIII isLicenseExpired(): false");
    	return false;

    }
}
