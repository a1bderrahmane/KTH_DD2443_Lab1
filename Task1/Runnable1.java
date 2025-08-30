public  class Runnable1 implements Runnable
{
 
    @Override
    public void run()
    {
        increment();
    }
    private  void increment()
    {
        for(int i=0;i<Main.NbIterations;i++)
        {
            Main.x++;
        }
    }
}