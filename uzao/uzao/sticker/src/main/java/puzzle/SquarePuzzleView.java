package puzzle;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author wupanjie
 */
public class SquarePuzzleView extends PuzzleView {
  public SquarePuzzleView(Context context) {
    super(context);
  }

  public SquarePuzzleView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SquarePuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int width = getMeasuredWidth();
    int height = getMeasuredHeight();
//    取小的作为测量结果的正方形边长
    int length = width > height ? height : width;

    setMeasuredDimension(length, length);

  }

}
