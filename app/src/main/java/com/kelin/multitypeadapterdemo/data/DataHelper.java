package com.kelin.multitypeadapterdemo.data;

import com.kelin.multitypeadapterdemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;

/**
 * 创建人 kelin
 * 创建时间 2017/4/11  上午10:06
 * 版本 v 1.0.0
 */

public class DataHelper {
    private List<Integer> icons = new ArrayList<>();

    private Random mRandom;
    private List<String> names;
    private ArrayList<String> countryList;

    private static class Instance{
        static DataHelper instance = new DataHelper();
    }

    {
        mRandom = new Random(System.currentTimeMillis());
        //男头像
        icons.add(R.mipmap.ic_man_1);
        icons.add(R.mipmap.ic_man_2);
        icons.add(R.mipmap.ic_man_3);
        icons.add(R.mipmap.ic_man_4);
        icons.add(R.mipmap.ic_man_5);
        icons.add(R.mipmap.ic_man_6);
        icons.add(R.mipmap.ic_man_7);
        icons.add(R.mipmap.ic_man_8);
        icons.add(R.mipmap.ic_man_9);
        icons.add(R.mipmap.ic_man_10);
        icons.add(R.mipmap.ic_man_11);
        icons.add(R.mipmap.ic_man_12);
        icons.add(R.mipmap.ic_man_13);
        icons.add(R.mipmap.ic_man_14);
        icons.add(R.mipmap.ic_man_15);
        icons.add(R.mipmap.ic_man_16);
        icons.add(R.mipmap.ic_man_17);
        icons.add(R.mipmap.ic_man_18);
        icons.add(R.mipmap.ic_man_19);
        //女头像
        icons.add(R.mipmap.ic_woman_1);
        icons.add(R.mipmap.ic_woman_2);
        icons.add(R.mipmap.ic_woman_3);
        icons.add(R.mipmap.ic_woman_4);
        icons.add(R.mipmap.ic_woman_5);
        icons.add(R.mipmap.ic_woman_6);
        icons.add(R.mipmap.ic_woman_7);
        icons.add(R.mipmap.ic_woman_8);
        icons.add(R.mipmap.ic_woman_9);
        icons.add(R.mipmap.ic_woman_10);
        icons.add(R.mipmap.ic_woman_11);
        icons.add(R.mipmap.ic_woman_12);
        icons.add(R.mipmap.ic_woman_13);
        icons.add(R.mipmap.ic_woman_14);
        icons.add(R.mipmap.ic_woman_15);
        icons.add(R.mipmap.ic_woman_16);
        icons.add(R.mipmap.ic_woman_17);
        icons.add(R.mipmap.ic_woman_18);
        icons.add(R.mipmap.ic_woman_19);
        icons.add(R.mipmap.ic_woman_20);
        icons.add(R.mipmap.ic_woman_21);
        icons.add(R.mipmap.ic_woman_22);
        icons.add(R.mipmap.ic_woman_23);
        icons.add(R.mipmap.ic_woman_24);
        icons.add(R.mipmap.ic_woman_25);
        icons.add(R.mipmap.ic_woman_26);

        String[] ns = "阿比盖尔,艾比,艾达,阿德莱德,艾德琳,亚历桑德拉,艾丽莎,艾米,爱丽丝,艾琳娜,艾莉森,阿曼达,艾美,安伯,阿纳斯塔西娅,安德莉亚,安吉拉,安吉莉亚,安吉莉娜,安妮,安尼塔,艾莉尔,阿普里尔,艾许莉,阿维娃,笆笆拉,贝亚特,比阿特丽斯,贝基,贝蒂,布兰奇,邦妮,布伦达,卡米尔,莰蒂丝,卡瑞娜,卡门,凯罗尔,卡罗琳,凯丽,凯莉,卡桑德拉,凯西,凯瑟琳,凯茜,切尔西,沙琳,夏洛特,切莉,雪莉尔,克莉丝,克里斯蒂娜,克里斯汀,克里斯蒂,辛迪,克劳迪娅,克莱门特,克劳瑞丝,康妮,康斯坦斯,科拉,科瑞恩,科瑞斯特尔,戴茜,达芙妮,达茜,黛比,黛博拉,黛布拉,黛米,黛安娜,德洛丽丝,堂娜,桃瑞丝,伊迪丝,伊迪萨,伊莱恩,埃莉诺,伊丽莎白,埃拉,爱伦,艾莉,艾米瑞达,艾米丽,艾玛,伊妮德,埃尔莎,埃莉卡,爱斯特尔,爱丝特,尤杜拉,伊娃,伊芙,芬妮,菲奥纳,弗郎西丝,弗雷德里卡,弗里达,吉娜,吉莉安,格拉蒂丝,格罗瑞娅,格瑞丝,格瑞塔,格温多琳,汉娜,海伦娜,海伦,赫柏,海蒂,英格丽德,爱沙拉,艾琳,艾丽丝,艾维,杰奎琳,詹米,珍妮特,姬恩,杰西卡,杰西,詹妮弗,詹妮,姬尔,琼,乔安娜,乔斯林,约瑟芬,乔茜,乔伊,乔伊斯,朱迪丝,朱蒂,朱莉娅,朱莉安娜,朱莉,朱恩,凯琳,卡瑞达,凯瑟琳,凯特,凯西,Cathy,卡特里娜,Katherine,凯莉,基蒂,Catherine,莱瑞拉,Laura,劳拉,莉娜,Lydia,莉迪娅,莉莲,Linda,琳达,丽莎,莉兹,罗琳,Louisa路易莎,Louise,路易丝,Lucia,露西娅,Lucy,露茜,Lucine,露西妮,Lulu露露,Lynn,林恩,Maggie,玛姬,Mamie,玛米,曼达,曼迪,玛格丽特,玛丽亚,Martha,玛莎,Mary,玛丽,Matilda,玛蒂尔达,莫琳,Mavis,梅维丝,玛克辛,May,梅,Mayme,梅米,梅甘,梅琳达,Melissa,梅利莎,Melody,美洛蒂,默西迪丝,梅瑞狄斯,米歇尔,Milly,米莉,Miranda,米兰达,Miriam,米里亚姆,Miya,米娅,Molly,茉莉,Monica,莫尼卡,Nancy,南茜,Natalie,娜塔莉,Natasha,娜塔莎,Nicole,妮可,Nikita,尼基塔,Nina,尼娜,奥琳娜,Oprah,奥帕,Pamela,帕梅拉,保拉,波琳,珀尔,帕姬,菲洛米娜,Phoebe,菲比,Phyllis,菲丽丝,Polly,波莉,Priscilla,普里西拉,Quentina,昆蒂娜,Rachel,雷切尔,Rebecca,丽贝卡,Regina,瑞加娜,Rita,丽塔,Rose,罗丝,Roxanne,洛克萨妮,Ruth,露丝,Sabrina,萨布丽娜,Sandra,桑德拉,Samantha,萨曼莎,Sandy,桑迪,Sarah,莎拉,Selma,塞尔玛,Selina,塞琳娜,Serena,塞丽娜,莎伦,希拉,雪莉,Silvia,西尔维亚,Sonia,索尼亚,Stacy,丝塔茜,丝特拉,Stephanie,斯蒂芬妮,Sue,苏,Sunny,萨妮,Susan,苏珊,塔玛拉,苔米,苔丝,Teresa,特莉萨,Tiffany,蒂凡妮,Tina,蒂娜,Tracy,特蕾西,Vanessa,温妮莎,Vicky,维姬,Victoria,维多利亚,Vivian,薇薇安,Wanda,旺达,Wendy,温蒂,Winnie,温妮,Yolanda,尤兰达,Yvette,伊薇特,Yvonne,伊温妮,Zoey,佐伊".split(",");
        names = new ArrayList<>(Arrays.asList(ns));

        String[] cs = "中国,蒙古,朝鲜,韩国,日本,越南,老挝,柬埔寨,缅甸,泰国,马来西亚,新加坡,文莱,菲律宾,印度尼西亚,东帝汶,尼泊尔,不丹,孟加拉国,印度,斯里兰卡,马尔代夫,巴基斯坦,阿富汗,伊朗,科威特,沙特阿拉伯,巴林,卡塔尔,阿联酋,阿曼,也门,伊拉克,叙利亚,黎巴嫩,约旦,巴勒斯坦,以色列,塞浦路斯,土耳其,乌兹别克斯坦,哈萨克斯坦,吉尔吉斯斯坦,塔吉克斯坦,亚美尼亚,土库曼斯坦,阿塞拜疆,格鲁吉亚,冰岛,丹麦,挪威,瑞典,芬兰,俄罗斯,乌克兰,白俄罗斯,摩尔多瓦,立陶宛,爱沙尼亚,拉脱维亚,波兰,捷克,匈牙利,德国,奥地利,列支敦士登,瑞士,荷兰,比利时,卢森堡,英国,爱尔兰,法国,摩纳哥,安道尔,西班牙,葡萄牙,意大利,梵蒂冈,圣马力诺,马耳他,克罗地亚,斯洛伐克,斯洛文尼亚,波黑,马其顿,塞尔维亚,黑山,科索沃,罗马尼亚,保加利亚,阿尔巴尼亚,希腊,埃及,利比亚,突尼斯,阿尔及利亚,摩洛哥,毛里塔尼亚,塞内加尔,冈比亚,马里,布基纳法索,佛得角,几内亚比绍,几内亚,塞拉里昂,利比里亚,科特迪瓦,加纳,多哥,贝宁,尼日尔,尼日利亚,喀麦隆,赤道几内亚,乍得,中非,苏丹,埃塞俄比亚,吉布提,索马里,肯尼亚,乌干达,坦桑尼亚,卢旺达,布隆迪,刚果,加蓬,圣多美和普林西比,安哥拉,赞比亚,马拉维,莫桑比克,科摩罗,马达加斯加,塞舌尔,毛里求斯,津巴布韦,博茨瓦纳,纳米比亚,南非,斯威士兰,莱索托,厄立特里亚,澳大利亚,新西兰,巴布亚新几内亚,所罗门群岛,瓦努阿图,,斐济,基里巴斯,瑙鲁,密克罗尼西亚,马绍尔群岛,图瓦卢,萨摩亚,纽埃-纽埃,帕劳,汤加,加拿大,美国,墨西哥,危地马拉,伯利兹,萨尔瓦多,洪都拉斯,尼加拉瓜,哥斯达黎加,巴拿马,巴哈马,古巴,牙买加,海地,多米尼加共和国,圣基茨和尼维斯,安提瓜和巴布达,多米尼克国,圣卢西亚,圣文森特和格林纳丁斯,巴巴多斯,格林纳达,特立尼达和多巴哥,哥伦比亚,委内瑞拉,圭亚那,苏里南,厄瓜多尔,秘鲁,巴西,玻利维亚,智利,阿根廷,巴拉圭,乌拉圭".split(",");
        countryList = new ArrayList<>(Arrays.asList(cs));
    }

    private DataHelper() {
    }

    public static DataHelper getInstance() {
        return Instance.instance;
    }

    public Observable<List<Person>> getPersons() {
        return Observable.create(new Observable.OnSubscribe<List<Person>>() {
            @Override
            public void call(Subscriber<? super List<Person>> subscriber) {
                List<Person> list = new ArrayList<Person>(1000);
                for (int i = 0; i < 1000; i++) {
                    list.add(new Person(icons.get(mRandom.nextInt(icons.size())), names.get(mRandom.nextInt(names.size())), countryList.get(mRandom.nextInt(countryList.size())), mRandom.nextInt(10) + 20, mRandom.nextInt(30) + 150, mRandom.nextInt(40) + 45, Person.Sex.UNKNOWN));
                }
                subscriber.onStart();
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<People> getManAndWoman() {
        return Observable.create(new Observable.OnSubscribe<People>() {
            @Override
            public void call(Subscriber<? super People> subscriber) {
                List<Person> m = new ArrayList<Person>();
                List<Person> w = new ArrayList<Person>();
                int location;
                Person object;
                for (int i = 0; i < 70; i++) {
                    location = mRandom.nextInt(icons.size());
                    object = new Person(icons.get(location), names.get(mRandom.nextInt(names.size())), countryList.get(mRandom.nextInt(countryList.size())), mRandom.nextInt(10) + 20, mRandom.nextInt(30) + 150, mRandom.nextInt(40) + 45, location < 19 ? Person.Sex.MAN : Person.Sex.WOMAN);
                    if (location < 19) {
                        m.add(object);
                    } else {
                        w.add(object);
                    }
                }
                subscriber.onStart();
                subscriber.onNext(new People(m, w));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<Classs>> getClassList() {
        return Observable.create(new Observable.OnSubscribe<List<Classs>>() {
            @Override
            public void call(Subscriber<? super List<Classs>> subscriber) {
                String[] c = new String[]{"一年1班", "一年2班", "一年3班","二年1班", "二年2班", "三年1班", "三年2班", "三年3班","四年1班", "四年2班", "四年3班","五年1班", "五年2班", "六年1班", "六年2班", "六年3班","文学班", "地理班", "舞蹈班","编程班"};
                List<Classs> classList = new ArrayList<Classs>(20);
                int count;
                int location;
                List<Person> list;
                for (int i = 0; i < 20; i++) {
                    count = mRandom.nextInt(10) + 10;
                    list = new ArrayList<Person>(count);
                    for (int x = 0; x < count; x++) {
                        location = mRandom.nextInt(icons.size());
                        list.add(new Person(icons.get(location), names.get(mRandom.nextInt(names.size())), countryList.get(mRandom.nextInt(countryList.size())), mRandom.nextInt(10) + 20, mRandom.nextInt(30) + 150, mRandom.nextInt(40) + 45, location < 19 ? Person.Sex.MAN : Person.Sex.WOMAN));
                    }
                    classList.add(new Classs(c[i], count, list));
                }
                subscriber.onStart();
                subscriber.onNext(classList);
                subscriber.onCompleted();
            }
        });
    }
}
