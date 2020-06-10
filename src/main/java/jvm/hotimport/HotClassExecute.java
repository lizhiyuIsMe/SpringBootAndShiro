package jvm.hotimport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//查看文件最后修改时间,如果时间变了则重新加载class
public class HotClassExecute implements Runnable {

    public static Map<String, Long> map = new HashMap<String, Long>();
    //项目路径
    public static String projectPath="";
    //java路径
    public static String javaPath;
    public static MyHotClassLoader myHotClassLoader;

    public HotClassExecute() throws IOException {
        File file = new File("");
        this.projectPath=file.getCanonicalPath();
        this.javaPath =projectPath+File.separator +"src"+File.separator +"main"+File.separator+"java";
        myHotClassLoader=new MyHotClassLoader();
    }

    @Override
    public void run() {
        //循环遍历所有java目录下的 .java文件
        File parent = new File(this.javaPath);
        //将所有的文件的修改时间放到map中
        execute(parent,"lastTimeAddTime");
        while (true) {
            //如果是新文件则添加到Map中
            //如果不是新文件看修改时间是否改变  如果改变则重新加载这个类
            execute(parent,"lastTimeUpdate");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


        //将所有的文件的修改时间放到map中
        private void execute(File parent,String type){
            if (parent.isDirectory()) {
                File[] files = parent.listFiles();
                for (File file : files) {
                    execute(file,type);
                }
            } else {
                //获得文件的修改时间
                long lastModifiedTime = parent.lastModified();
                //全路径
                String absPath = parent.getAbsolutePath();
                if ("lastTimeAddTime".equals(type)) {
                    map.put(absPath,lastModifiedTime);
                }else{
                    lastTimeUpdate(lastModifiedTime,absPath);
                }
            }
        }
        public void lastTimeUpdate(long lastModifiedTime, String absPath){
            //如果是新文件则添加到Map中
            if(map.containsKey(absPath)){
                if(map.get(absPath).equals(lastModifiedTime)){
                   //是什么都不做
                    return;
                }else{
                    //对这个文件加载
                    try {
                        absPath = absPath.replace(".java", "");
                        String substring = absPath.substring(72);
                        substring = substring.replace("\\", ".");
                        Class<?> aClass = myHotClassLoader.loadClass(substring);
                        People o = (People) aClass.newInstance();
                        System.out.println(aClass.getClassLoader());
                        o.say();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else{
                //放入map中,等待下次修改
                map.put(absPath,0l);
            }
        }
}
