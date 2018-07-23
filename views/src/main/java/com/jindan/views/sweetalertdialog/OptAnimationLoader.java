package com.jindan.views.sweetalertdialog;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Author zhaolei
 * Date 2018/4/12
 * Description desc
 */

public class OptAnimationLoader {
    public OptAnimationLoader() {
    }

    public static Animation loadAnimation(Context context, int id) throws Resources.NotFoundException {
        XmlResourceParser parser = null;

        Animation var3;
        try {
            Resources.NotFoundException rnf;
            try {
                parser = context.getResources().getAnimation(id);
                var3 = createAnimationFromXml(context, parser);
            } catch (XmlPullParserException var9) {
                rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
                rnf.initCause(var9);
                throw rnf;
            } catch (IOException var10) {
                rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
                rnf.initCause(var10);
                throw rnf;
            }
        } finally {
            if(parser != null) {
                parser.close();
            }

        }

        return var3;
    }

    private static Animation createAnimationFromXml(Context c, XmlPullParser parser) throws XmlPullParserException, IOException {
        return createAnimationFromXml(c, parser, (AnimationSet)null, Xml.asAttributeSet(parser));
    }

    private static Animation createAnimationFromXml(Context c, XmlPullParser parser, AnimationSet parent, AttributeSet attrs) throws XmlPullParserException, IOException {
        Animation anim = null;
        int depth = parser.getDepth();

        int type;
        while(((type = parser.next()) != 3 || parser.getDepth() > depth) && type != 1) {
            if(type == 2) {
                String name = parser.getName();
                if(name.equals("set")) {
                    anim = new AnimationSet(c, attrs);
                    createAnimationFromXml(c, parser, (AnimationSet)anim, attrs);
                } else if(name.equals("alpha")) {
                    anim = new AlphaAnimation(c, attrs);
                } else if(name.equals("scale")) {
                    anim = new ScaleAnimation(c, attrs);
                } else if(name.equals("rotate")) {
                    anim = new RotateAnimation(c, attrs);
                } else if(name.equals("translate")) {
                    anim = new TranslateAnimation(c, attrs);
                } else {
                    try {
                        anim = (Animation)Class.forName(name).getConstructor(new Class[]{Context.class, AttributeSet.class}).newInstance(new Object[]{c, attrs});
                    } catch (Exception var9) {
                        throw new RuntimeException("Unknown animation name: " + parser.getName() + " error:" + var9.getMessage());
                    }
                }

                if(parent != null) {
                    parent.addAnimation((Animation)anim);
                }
            }
        }

        return (Animation)anim;
    }
}
