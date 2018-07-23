package com.jindan.views.smartImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint("AppCompatCustomView")
public class SmartImageView extends ImageView {
    private static final int LOADING_THREADS = 6;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(LOADING_THREADS);

    private SmartImageTask currentTask;
    
    private Context context;
    

    public SmartImageView(Context context) {    	
        super(context);        
        this.context = context;
    }

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    // Helpers to set image by URL
    public void setImageUrl(String url) {
        setImage(new WebImage(context,url));
    }
    
    /**
     *
     * @param url
     * @param isBackground
     */
    public void setImageUrl(String url, Integer loadingResource, boolean isBackground) {
    	setImage(new WebImage(context,url), null, loadingResource, isBackground, null);
    }
    
    public void setImageUrl(String url, SmartImageTask.OnCompleteListener completeListener) {
        setImage(new WebImage(context,url), completeListener);
    }

    public void setImageUrl(String url, final Integer fallbackResource) {
        setImage(new WebImage(context,url), fallbackResource);
    }

    public void setImageUrl(String url, final Integer fallbackResource, SmartImageTask.OnCompleteListener completeListener) {
        setImage(new WebImage(context,url), fallbackResource, completeListener);
    }

    public void setImageUrl(String url, final Integer fallbackResource, final Integer loadingResource) {
        setImage(new WebImage(context,url), fallbackResource, loadingResource);
    }

    public void setImageUrl(String url, final Integer fallbackResource, final Integer loadingResource, SmartImageTask.OnCompleteListener completeListener) {
        setImage(new WebImage(context,url), fallbackResource, loadingResource, completeListener);
    }


    // Helpers to set image by contact address book id
    public void setImageContact(long contactId) {
        setImage(new ContactImage(contactId));
    }

    public void setImageContact(long contactId, final Integer fallbackResource) {
        setImage(new ContactImage(contactId), fallbackResource);
    }

    public void setImageContact(long contactId, final Integer fallbackResource, final Integer loadingResource) {
        setImage(new ContactImage(contactId), fallbackResource, fallbackResource);
    }


    // Set image using SmartImage object
    public void setImage(final SmartImage image) {
        setImage(image, null, null, null);
    }

    public void setImage(final SmartImage image, final SmartImageTask.OnCompleteListener completeListener) {
        setImage(image, null, null, completeListener);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource) {
        setImage(image, fallbackResource, fallbackResource, null);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource, SmartImageTask.OnCompleteListener completeListener) {
        setImage(image, fallbackResource, fallbackResource, completeListener);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource, final Integer loadingResource) {
        setImage(image, fallbackResource, loadingResource, null);
    }
    
    public void setImage(final SmartImage image, final Integer fallbackResource, final Integer loadingResource, final boolean isBackground, final SmartImageTask.OnCompleteListener completeListener) {
    	// Set a loading resource
        if(loadingResource != null){
        	if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN && isBackground) {
        		setBackgroundResource(loadingResource);
        	} else {
        		setImageResource(loadingResource);
        	}
        }

        // Cancel any existing tasks for this image view
        if(currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }

        // Set up the new task
        currentTask = new SmartImageTask(getContext(), image);
        currentTask.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler() {
            @SuppressLint("NewApi")
			@Override
            public void onComplete(Bitmap bitmap) {
                if(bitmap != null) {
                	if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN && isBackground) {
                		setBackground(new BitmapDrawable(bitmap));
                	} else {
                		setImageBitmap(bitmap);
                	}
                } else {
                    // Set fallback resource
                    if(fallbackResource != null) {
                    	if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN && isBackground) {
                    		setBackgroundResource(fallbackResource);
                    	} else {
                    		setImageResource(fallbackResource);
                    	}
                    }
                }

                if(completeListener != null){
                    completeListener.onComplete();
                }
            }
        });

        // Run the task in a threadpool
        threadPool.execute(currentTask);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource, final Integer loadingResource, final SmartImageTask.OnCompleteListener completeListener) {
        // Set a loading resource
        if(loadingResource != null){
            setImageResource(loadingResource);
        }

        // Cancel any existing tasks for this image view
        if(currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }

        // Set up the new task
        currentTask = new SmartImageTask(getContext(), image);
        currentTask.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler() {
            @SuppressLint("NewApi")
			@Override
            public void onComplete(Bitmap bitmap) {
                if(bitmap != null) {
                	setImageBitmap(bitmap);
                } else {
                    // Set fallback resource
                    if(fallbackResource != null) {
                    	setImageResource(fallbackResource);
                    }
                }

                if(completeListener != null){
                    completeListener.onComplete();
                }
            }
        });

        // Run the task in a threadpool
        threadPool.execute(currentTask);
    }

    public static void cancelAllTasks() {
        threadPool.shutdownNow();
        threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
    }
    
}