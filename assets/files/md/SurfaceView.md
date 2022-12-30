Q:介绍下android平台的SurfaceView

SurfaceView 是一个特殊的 View，允许你在单独的窗口中绘制图形。这是很有用的，因为它允许你在后台线程中进行绘图，而不会影响 UI 线程的性能。

使用 SurfaceView 的一个常见用例是在 Android 应用程序中播放视频。这是因为视频播放可能需要大量的 CPU 资源，如果在 UI 线程中进行，可能会导致应用程序卡顿或掉帧。使用 SurfaceView，您可以将视频播放的操作放在后台线程中，而不会影响 UI 线程的性能。

要使用 SurfaceView，您需要创建一个继承自 SurfaceView 的自定义 View，并实现 SurfaceHolder.Callback 接口。然后，您可以使用 getHolder() 方法获取 SurfaceHolder 对象，并使用它来控制 SurfaceView 的行为。例如，您可以使用 SurfaceHolder.addCallback() 方法注册一个回调函数，在 SurfaceView 可用或不可用时调用。

一旦您有了 SurfaceView，您就可以使用 Canvas 和 Paint 类在 SurfaceView 上绘图。例如，您可以使用 Canvas.drawCircle() 方法在 SurfaceView 上绘制圆形，或使用 Canvas.drawBitmap() 方法在 SurfaceView 上绘制位图。

为了在 SurfaceView 上进行绘图，您需要创建一个后台线程。在该线程中，您可以使用 SurfaceHolder.lockCanvas 对象来绘图。首先，您需要调用 SurfaceHolder.lockCanvas() 方法获取 Canvas 对象，然后使用该对象的绘图方法进行绘图，最后调用 SurfaceHolder.unlockCanvasAndPost() 方法将绘图结果发布到 SurfaceView 上。

例如，下面是一个简单的后台线程，它使用 SurfaceView 绘制一个圆形：

```java
class DrawingThread extends Thread {
  private SurfaceHolder surfaceHolder;
  private boolean isRunning = false;

  public DrawingThread(SurfaceHolder surfaceHolder) {
    this.surfaceHolder = surfaceHolder;
  }

  public void setRunning(boolean isRunning) {
    this.isRunning = isRunning;
  }

  @Override
  public void run() {
    while (isRunning) {
      Canvas canvas = surfaceHolder.lockCanvas();
      if (canvas != null) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(100, 100, 50, paint);
        surfaceHolder.unlockCanvasAndPost(canvas);
      }
    }
  }
}
```
请注意，在绘制时，您需要注意性能。如果您的后台线程中的绘图操作过于频繁，可能会导致应用程序卡顿或掉帧。因此，您应该尽量减少绘图操作的数量，并使用合适的优化技术来提高性能。

总的来说，SurfaceView 是一个非常有用的 View，允许您在后台线程中进行绘图，而不会影响 UI 线程的性能。使用 SurfaceView，您可以在 Android 应用程序中播放视频，或执行其他可能需要大量 CPU 资源的操作。