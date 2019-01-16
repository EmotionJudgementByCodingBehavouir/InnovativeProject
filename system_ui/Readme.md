# Emotion Judgement
==============================================
———————————————————version 1.0————————————————————<br>
假装有一个readme
——————————————————————————————————————————————————<br>
# 重要内容请仔细查看
* 本项目共用一个Github仓库，账户和密码请保管好。
* 首先请使用下面的命令，将git本地的账户设置成本项目的账户
<pre>
<code>
	git config --global user.name "YOURUSERNAME" 
	git config --global user.email "YOUREMAIL"
</code>
</pre>
* 然后在Windows控制面板->用户账户->凭据管理中，你原来在本机的github用户的缓存删除，否则git还是会用你之前的账户进行push，pull等操作。可以参考这篇文章<https://blog.csdn.net/lqlqlq007/article/details/79065095>
* 在每次进行编码前，首先执行git pull保持本地仓库与远端仓库一致。  
编码结束后，执行git push上传结果。
* 编码完后请在代码说明里按包、类的结果写出代码的接口。
* 如果有疑问及时沟通
# Java版本：Java 1.8.0
# 项目生成工具：Maven
## 依赖包
* java-string-similarity 1.1.0
* weka-stable 3.8.0
* matplot4j 0.4.0 (这个图形库需要python环境 + matplotlib)
* （若有增加，请及时在pom文件和readme中添加）
# 分工
## 王雅薪
* 图形界面设计
* 功能整合
## 崔子寒
* 可视化展示
* 协助王雅薪进行功能整合
## 侍林天
* 数据预处理
* 模型分类
* 协助王雅薪进行功能整合

# 代码说明
## preprocess包
### GitLogReader类
* 构造函数 public GitLogReader(String fileName)，fileName为gitlog文件路径
* public IntermediateLog getIntermediateLog()从读取的gitlog中获得一个解析中间结果
### IntermediateLog类
* 从gitlog文件中解析出的中间结果，仅用String类型包含了必要的信息。
* 该类仅为方便数据的处理，无须过多关注。
### IntermediateCommit类
* IntermediateLog类里有一个ArrayList<IntermediateCommit>的数据成员
### PatternedCommit类
* PatternedCommit类保存了gitlog里每一次commit的具体信息。
### PatternedLog类
* PatternedLog类由一系列按时间顺序从小到大排序的PatternedCommit对象组成。
<pre>
<code>
	private ArrayList&lt;PatternedCommit&gt; log;
</code>
</pre>
* public Iterator&lt;PatternedCommit&gt; getPatternedLogIterator()  
获得PatternedLog里PatternedCommit数组的迭代器。
### CompileLog类
* 使用一个Date数组，按时间顺序保存了编译记录。
* public CompileLog(BufferedReader br)，构造函数。参数为编译记录文件。
### Slice类
* 一段时间内，gitlog数据的切片信息。
### LogSlicer类
* 将gitlog，compilelog按固定时间切片。
* public ArrayList&lt;Slice&gt; getLogSlice(PatternedLog pLog, CompileLog cLog, int interval)
### 代码示例
<pre>
<code>
	public static void main(String[] args) {
        try {
            IntermediateLog log = new GitLogReader("E:\\PA_project\\getlog\\Log\\151242017\\detail\\Week5.log").getIntermediateLog();
            PatternedLog pLog = new PatternedLog(log);
            CompileLog cLog = new CompileLog(new BufferedReader(new FileReader("E:\\PA_project\\Compile_data\\151242017\\data.txt")));
            ArrayList<Slice> slices = new LogSlicer().getLogSlice(pLog, cLog, 15);
            PredictModel model = new PredictModel("model/model.model");
            for (Slice s : slices) {
                System.out.println(model.predict(s));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
</code>
</pre>
## model包
### emotion包
#### Emotion枚举
<pre>
<code>
public enum Emotion {
    FLOW, NOTFLOW;
}
</code>
</pre>
#### EmotionLog类
* 保存了一个情绪记录和对应的时间
* <pre>
<code>
public class EmotionLog {
    	private Date logTime;
    	private Emotion emotion;
    	public EmotionLog(Date date, Emotion emotion) {
        		...
    	}
</code>
</pre>
* public Date getLogTime()  
获得时间
* public Emotion getEmotion()  
获得情绪
### predict包
#### PredictModel类
* 分类模型
* public PredictModel(String modelPath)  
通过文件，加载模型
* public EmotionLog predict(Slice slice)  
传入一个Slice，得到一个EmotionLog
