package com.dudubaika.ui.view;

import android.support.v4.app.Fragment;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
	/*private String mImageUrl;
	private ImageView mImageView;

 
	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();
 
		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);
 
		return f;
	}
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url")
				: null;
	}
 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment,
				container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);
 
		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
 
			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});
 
		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}*/
 

}