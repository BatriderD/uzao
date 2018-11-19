package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * description: MKU载体信息实体类
 * author : ZP
 * date: 2018/1/9 0009.
 */

public class MKUCarrierBean implements Serializable {
    /**
     * mkus : {"changedFields":"","craftGroupId":"940516857427910656","craftGroups":[{"aspectId":"940516856630992896","aspectName":"角度1","changedFields":"","craftGroupId":null,"craftId":null,"craftModels":[{"changedFields":"","craftCode":"00006","craftName":"喷绘","description":"喷绘","extend1":"","extend2":"","extend3":"","lockDate":"1512978067000","lockStatus":"N","lockUserId":"908508635489906688","recDate":"1514280327000","recStatus":"A","recUserId":"931419998910464000","sequenceNBR":"940124271571705856","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00011","craftName":"石雕","description":"石雕，指用各种可雕、可刻的石头，创造出具有一定空间的可视、可触的艺术形象，借以反映社会生活、表达艺术家的审美感受、审美情感、审美理想的艺术。常用的石材有花岗石、大理石、青石、砂石等。石材质量坚硬耐风化，是大型纪念性雕塑的主要材料。2008年入选第二批国家级非物质文化遗产名录。","extend1":"","extend2":"","extend3":"","lockDate":"1512982660000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1513585405000","recStatus":"A","recUserId":"931419998910464000","sequenceNBR":"940143537310179328","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00007","craftName":"切割","description":"切割","extend1":"","extend2":"","extend3":"","lockDate":"1512978142000","lockStatus":"N","lockUserId":"908508635489906688","recDate":"1514280538000","recStatus":"A","recUserId":"931419998910464000","sequenceNBR":"940124585662160896","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00010","craftName":"焊接","description":"焊接","extend1":"","extend2":"","extend3":"","lockDate":"1512982510000","lockStatus":"N","lockUserId":"908508635489906688","recDate":"1512982510000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"940142905434087424","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00009","craftName":"印刷","description":"印刷","extend1":"","extend2":"","extend3":"","lockDate":"1512982376000","lockStatus":"N","lockUserId":"908508635489906688","recDate":"1512982376000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"940142342432661504","thumbnail":"940142311805923328_resize.jpg","userName":"","video":""},{"changedFields":"","craftCode":"00008","craftName":"烫金","description":"烫金","extend1":"","extend2":"","extend3":"","lockDate":"1512980206000","lockStatus":"N","lockUserId":"908508635489906688","recDate":"1513309646000","recStatus":"A","recUserId":"909956542105726976","sequenceNBR":"940128550177021952","thumbnail":"undefined","userName":"","video":"940128494640279552.mp4"},{"changedFields":"","craftCode":"00005","craftName":"陶瓷","description":"陶瓷，英语：china。中国人早在约公元前8000－2000年（新石器时代）就发明了陶器。用陶土烧制的器皿叫陶器，用瓷土烧制的器皿叫瓷器。陶瓷则是陶器，炻器和瓷器的总称。古人称陶瓷为瓯。凡是用陶土和瓷土这两种不同性质的粘土为原料，经过配料、成型、干燥、焙烧等工艺流程制成的器物都可以叫陶瓷。","extend1":"","extend2":"","extend3":"","lockDate":"1512977068000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1513585391000","recStatus":"A","recUserId":"931419998910464000","sequenceNBR":"940120081034145792","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00004","craftName":"汝瓷","description":"汝瓷，因产于汝州市故得名汝瓷，为国家地理标志保护产品。汝瓷创烧于北宋晚期，为宫廷御用瓷器。金灭北宋后，汝瓷也随之消亡。其开窑时间前后只有二十年，由于烧造时间短暂，传世亦不多，在南宋时，汝瓷已经非常稀有。现今存世的汝瓷，一般认为有65件，其中台北故宫博物院23件，北京故宫博物院17件，上海博物馆8件，英国戴维基金会（Percival David Foundation of Chinese Art）7件，其他散藏于美、日等博物馆和私人收藏约10件。","extend1":"","extend2":"","extend3":"","lockDate":"1512973591000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1513585381000","recStatus":"A","recUserId":"931419998910464000","sequenceNBR":"940105498793979904","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00003","craftName":"青铜器","description":" 青铜器（Bronze Ware）是由青铜合金（红铜与锡的合金）制成的器具，诞生于人类文明时期的青铜时代。青铜器在世界各地均有出现，是一种世界性文明的象征。最早的青铜器出现于6000年前的古巴比伦两河流域。苏美尔文明时期雕有狮子形象的大型铜刀是早期青铜器的代表。青铜器在2000多年前逐渐由铁器所取代。中国青铜器制作精美，在世界青铜器中享有极高的声誉和艺术价值，代表着中国4000多年青铜发展的高超技术与文化。","extend1":"","extend2":"","extend3":"","lockDate":"1512958573000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1513938982000","recStatus":"A","recUserId":"9527","sequenceNBR":"939446080498622464","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00002","craftName":"琉璃","description":" 琉璃，琉璃亦作\u201c瑠璃\u201d是用各种颜色（颜色是由各种稀有金属形成）的人造水晶（含24%的二氧化铅）为原料，是在1000多度的高温下烧制而成的。其色彩流云漓彩；其品质晶莹剔透、光彩夺目。中国古代最初制作琉璃的材料，是从青铜器铸造时产生的副产品中获得的，经过提炼加工然后制成琉璃。琉璃的颜色多种多样，古人也叫它\u201c五色石\u201d。古时由于民间很难得到，所以当时人们把琉璃甚至看成比玉器还要珍贵。2008年6月，琉璃烧制技艺入选国务院批准文化部确定的第二批国家级非物质文化遗产名录。目前市场上的琉璃主要是以南方（至台湾）为代表的脱蜡琉璃和以博山（淄博）为代表的手工琉璃为主。国内琉璃制作比较有名的团队分别是琉璃工房、人立琉璃、琉园等 。","extend1":"","extend2":"","extend3":"","lockDate":"1512816327000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1513557342000","recStatus":"A","recUserId":"909956542105726976","sequenceNBR":"939445851586093056","thumbnail":"942673404190814208.jpg","userName":"","video":""},{"changedFields":"","craftCode":"00001","craftName":"苏绣","description":"刺绣是针线在织物上绣制的各种装饰图案的总称。刺绣分丝线刺绣和羽毛刺绣两种。就是用针将丝线或其他纤维、纱线以一定图案和色彩在绣料上穿刺，以绣迹构成花纹的装饰织物。它是用针和线把人的设计和制作添加在任何存在的织物上的一种艺术。刺绣是中国民间传统手工艺之一，在中国至少有二三千年历史。中国刺绣主要有苏绣、湘绣、蜀绣和粤绣四大门类。刺绣的技法有：错针绣、乱针绣、网绣、满地绣、锁丝、纳丝、纳锦、平金、影金、盘金、铺绒、刮绒、戳纱、洒线、挑花等等，刺绣的用途主要包括生活和艺术装饰，如服装、床上用品、台布、舞台、艺术品装饰。","extend1":"","extend2":"","extend3":"","lockDate":"1512816017000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1514280547000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"939444550458425344","thumbnail":"","userName":"","video":""}],"description":"","extend1":"","extend2":"","extend3":"","mkuId":null,"recDate":null,"recStatus":"","recUserId":"","sequenceNBR":null,"spuId":null}],"description":"","extend1":"","extend2":"","extend3":"","maskGroupId":"940516857427910656","maskGroupName":"陶瓷白色80*80*45mm蒙版组","maskGroups":[{"aspectId":"940516856630992896","aspectName":"角度1","changedFields":"","description":"","editAreas":[{"border":"940548676621819904_resize.png","changedFields":"","defaultScale":"0.17","description":"","extend1":"","extend2":"","extend3":"","mark":"940548657382547456_resize.png","mask":"940548668220628992_resize.png","maskGroupId":"940516857427910656","maskId":"940548796541112320","minDpi":300,"mkuId":"940516857427910656","position":"{\"x\":234.2857142857143,\"y\":237.14285714285717,\"imgWidth\":1000,\"imgHeight\":1000,\"borderWidth\":\"573\",\"borderHeight\":\"583\"}","recDate":"1513079282000","recStatus":"A","recUserId":"908618890433585152","scale":"0.17","sequenceNBR":"940548796549500928","shade":"940548686839144448_resize.png","spuId":"910759623487700992"}],"extend1":"","extend2":"","extend3":"","maskGroupId":"940516857427910656","mkuId":"940516857427910656","recDate":"1513079282000","recStatus":"A","recUserId":"908618890433585152","sequenceNBR":"940548796541112320","spuId":"910759623487700992","thumbnail":"940548594673508352_resize.jpg"}],"mkuKey":"2125914304:639657837;2091153207:1238482404;202537481:231033075;","mkuValues":"材质:陶瓷;颜色:白色;规格:80*80*45mm;","recDate":"1513071667000","recStatus":"A","recUserId":"908618890433585152","referenceCraftGroupId":null,"referenceMaskGroupId":null,"sequenceNBR":"940516857427910656","spuId":"910759623487700992"}
     * sampleSpuModel : {"advanced":"N","aspectNames":[],"categoryPropertyTypes":[],"changedFields":"","component":"N","customCycle":null,"deliveryAddress":"陕西西安","description":"可定制款陶瓷白盘。","designCount":101,"designPropertyTypes":[],"designSpuIds":[],"designType":"2d","extend1":"","extend2":"","extend3":"","extraValue":null,"face":"N","isDesignInit":"","marketPrice":1,"marketPriceY":"0.01","mkuId":null,"mkuKey":"","mkuValues":"","propertyTypeCodes":[],"rcDeliveryAddress":"陕西西安","recDate":"1513587653000","recStatus":"A","recUserId":"908618890433585152","recommendedPrice":100,"recommendedPriceY":"1.00","sequenceNBR":"910759623487700992","skuId":null,"skus":[],"spuImages":[],"spuModel":{"agencyCode":"SUPER_ADMIN","categoryCode":"040223","changedFields":"","commentCount":0,"createDate":"1505976989000","description":"","displayPrice":1,"displayPriceY":"","extend1":"04","extend2":"0402","extend3":"厨具>厨房用具>碗/碟/盘","favoriteCount":1,"hasCollection":false,"lockDate":"1505976989000","lockStatus":"N","lockUserId":"908232819585626112","priority":1,"rcSpuCode":"040223187764100228","rcSpuName":"陶瓷盘子定制款","recDate":"1515210866000","recStatus":"A","recUserId":"9527","salesCount":1,"sequenceNBR":"910759623487700992","spuCode":"910759623449952256-S","spuName":"用来测试2D的抱枕载体载体载体","spuType":"sample","status":"published","supplierId":"909692344465362944","thumbnail":"937620620353589248.png","upvoteCount":0,"viewCount":0},"spuPropertyTypes":[],"spuRichTextImages":[],"storeNum":null,"supplierQuote":1900,"supplierQuoteY":"19.00","tags":[]}
     */

    private MkusBean mkus;
    private SampleSpuModelBean sampleSpuModel;

    public MkusBean getMkus() {
        return mkus;
    }

    public void setMkus(MkusBean mkus) {
        this.mkus = mkus;
    }

    public SampleSpuModelBean getSampleSpuModel() {
        return sampleSpuModel;
    }

    public void setSampleSpuModel(SampleSpuModelBean sampleSpuModel) {
        this.sampleSpuModel = sampleSpuModel;
    }

    public static class MkusBean implements Serializable {
        /**
         * changedFields :
         * craftGroupId : 940516857427910656
         * craftGroups : [{"aspectId":"940516856630992896","aspectName":"角度1","changedFields":"","craftGroupId":null,"craftId":null,"craftModels":[{"changedFields":"","craftCode":"00006","craftName":"喷绘","description":"喷绘","extend1":"","extend2":"","extend3":"","lockDate":"1512978067000","lockStatus":"N","lockUserId":"908508635489906688","recDate":"1514280327000","recStatus":"A","recUserId":"931419998910464000","sequenceNBR":"940124271571705856","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00011","craftName":"石雕","description":"石雕，指用各种可雕、可刻的石头，创造出具有一定空间的可视、可触的艺术形象，借以反映社会生活、表达艺术家的审美感受、审美情感、审美理想的艺术。常用的石材有花岗石、大理石、青石、砂石等。石材质量坚硬耐风化，是大型纪念性雕塑的主要材料。2008年入选第二批国家级非物质文化遗产名录。","extend1":"","extend2":"","extend3":"","lockDate":"1512982660000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1513585405000","recStatus":"A","recUserId":"931419998910464000","sequenceNBR":"940143537310179328","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00007","craftName":"切割","description":"切割","extend1":"","extend2":"","extend3":"","lockDate":"1512978142000","lockStatus":"N","lockUserId":"908508635489906688","recDate":"1514280538000","recStatus":"A","recUserId":"931419998910464000","sequenceNBR":"940124585662160896","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00010","craftName":"焊接","description":"焊接","extend1":"","extend2":"","extend3":"","lockDate":"1512982510000","lockStatus":"N","lockUserId":"908508635489906688","recDate":"1512982510000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"940142905434087424","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00009","craftName":"印刷","description":"印刷","extend1":"","extend2":"","extend3":"","lockDate":"1512982376000","lockStatus":"N","lockUserId":"908508635489906688","recDate":"1512982376000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"940142342432661504","thumbnail":"940142311805923328_resize.jpg","userName":"","video":""},{"changedFields":"","craftCode":"00008","craftName":"烫金","description":"烫金","extend1":"","extend2":"","extend3":"","lockDate":"1512980206000","lockStatus":"N","lockUserId":"908508635489906688","recDate":"1513309646000","recStatus":"A","recUserId":"909956542105726976","sequenceNBR":"940128550177021952","thumbnail":"undefined","userName":"","video":"940128494640279552.mp4"},{"changedFields":"","craftCode":"00005","craftName":"陶瓷","description":"陶瓷，英语：china。中国人早在约公元前8000－2000年（新石器时代）就发明了陶器。用陶土烧制的器皿叫陶器，用瓷土烧制的器皿叫瓷器。陶瓷则是陶器，炻器和瓷器的总称。古人称陶瓷为瓯。凡是用陶土和瓷土这两种不同性质的粘土为原料，经过配料、成型、干燥、焙烧等工艺流程制成的器物都可以叫陶瓷。","extend1":"","extend2":"","extend3":"","lockDate":"1512977068000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1513585391000","recStatus":"A","recUserId":"931419998910464000","sequenceNBR":"940120081034145792","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00004","craftName":"汝瓷","description":"汝瓷，因产于汝州市故得名汝瓷，为国家地理标志保护产品。汝瓷创烧于北宋晚期，为宫廷御用瓷器。金灭北宋后，汝瓷也随之消亡。其开窑时间前后只有二十年，由于烧造时间短暂，传世亦不多，在南宋时，汝瓷已经非常稀有。现今存世的汝瓷，一般认为有65件，其中台北故宫博物院23件，北京故宫博物院17件，上海博物馆8件，英国戴维基金会（Percival David Foundation of Chinese Art）7件，其他散藏于美、日等博物馆和私人收藏约10件。","extend1":"","extend2":"","extend3":"","lockDate":"1512973591000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1513585381000","recStatus":"A","recUserId":"931419998910464000","sequenceNBR":"940105498793979904","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00003","craftName":"青铜器","description":" 青铜器（Bronze Ware）是由青铜合金（红铜与锡的合金）制成的器具，诞生于人类文明时期的青铜时代。青铜器在世界各地均有出现，是一种世界性文明的象征。最早的青铜器出现于6000年前的古巴比伦两河流域。苏美尔文明时期雕有狮子形象的大型铜刀是早期青铜器的代表。青铜器在2000多年前逐渐由铁器所取代。中国青铜器制作精美，在世界青铜器中享有极高的声誉和艺术价值，代表着中国4000多年青铜发展的高超技术与文化。","extend1":"","extend2":"","extend3":"","lockDate":"1512958573000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1513938982000","recStatus":"A","recUserId":"9527","sequenceNBR":"939446080498622464","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00002","craftName":"琉璃","description":" 琉璃，琉璃亦作\u201c瑠璃\u201d是用各种颜色（颜色是由各种稀有金属形成）的人造水晶（含24%的二氧化铅）为原料，是在1000多度的高温下烧制而成的。其色彩流云漓彩；其品质晶莹剔透、光彩夺目。中国古代最初制作琉璃的材料，是从青铜器铸造时产生的副产品中获得的，经过提炼加工然后制成琉璃。琉璃的颜色多种多样，古人也叫它\u201c五色石\u201d。古时由于民间很难得到，所以当时人们把琉璃甚至看成比玉器还要珍贵。2008年6月，琉璃烧制技艺入选国务院批准文化部确定的第二批国家级非物质文化遗产名录。目前市场上的琉璃主要是以南方（至台湾）为代表的脱蜡琉璃和以博山（淄博）为代表的手工琉璃为主。国内琉璃制作比较有名的团队分别是琉璃工房、人立琉璃、琉园等 。","extend1":"","extend2":"","extend3":"","lockDate":"1512816327000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1513557342000","recStatus":"A","recUserId":"909956542105726976","sequenceNBR":"939445851586093056","thumbnail":"942673404190814208.jpg","userName":"","video":""},{"changedFields":"","craftCode":"00001","craftName":"苏绣","description":"刺绣是针线在织物上绣制的各种装饰图案的总称。刺绣分丝线刺绣和羽毛刺绣两种。就是用针将丝线或其他纤维、纱线以一定图案和色彩在绣料上穿刺，以绣迹构成花纹的装饰织物。它是用针和线把人的设计和制作添加在任何存在的织物上的一种艺术。刺绣是中国民间传统手工艺之一，在中国至少有二三千年历史。中国刺绣主要有苏绣、湘绣、蜀绣和粤绣四大门类。刺绣的技法有：错针绣、乱针绣、网绣、满地绣、锁丝、纳丝、纳锦、平金、影金、盘金、铺绒、刮绒、戳纱、洒线、挑花等等，刺绣的用途主要包括生活和艺术装饰，如服装、床上用品、台布、舞台、艺术品装饰。","extend1":"","extend2":"","extend3":"","lockDate":"1512816017000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1514280547000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"939444550458425344","thumbnail":"","userName":"","video":""}],"description":"","extend1":"","extend2":"","extend3":"","mkuId":null,"recDate":null,"recStatus":"","recUserId":"","sequenceNBR":null,"spuId":null}]
         * description :
         * extend1 :
         * extend2 :
         * extend3 :
         * maskGroupId : 940516857427910656
         * maskGroupName : 陶瓷白色80*80*45mm蒙版组
         * maskGroups : [{"aspectId":"940516856630992896","aspectName":"角度1","changedFields":"","description":"","editAreas":[{"border":"940548676621819904_resize.png","changedFields":"","defaultScale":"0.17","description":"","extend1":"","extend2":"","extend3":"","mark":"940548657382547456_resize.png","mask":"940548668220628992_resize.png","maskGroupId":"940516857427910656","maskId":"940548796541112320","minDpi":300,"mkuId":"940516857427910656","position":"{\"x\":234.2857142857143,\"y\":237.14285714285717,\"imgWidth\":1000,\"imgHeight\":1000,\"borderWidth\":\"573\",\"borderHeight\":\"583\"}","recDate":"1513079282000","recStatus":"A","recUserId":"908618890433585152","scale":"0.17","sequenceNBR":"940548796549500928","shade":"940548686839144448_resize.png","spuId":"910759623487700992"}],"extend1":"","extend2":"","extend3":"","maskGroupId":"940516857427910656","mkuId":"940516857427910656","recDate":"1513079282000","recStatus":"A","recUserId":"908618890433585152","sequenceNBR":"940548796541112320","spuId":"910759623487700992","thumbnail":"940548594673508352_resize.jpg"}]
         * mkuKey : 2125914304:639657837;2091153207:1238482404;202537481:231033075;
         * mkuValues : 材质:陶瓷;颜色:白色;规格:80*80*45mm;
         * recDate : 1513071667000
         * recStatus : A
         * recUserId : 908618890433585152
         * referenceCraftGroupId : null
         * referenceMaskGroupId : null
         * sequenceNBR : 940516857427910656
         * spuId : 910759623487700992
         */

        private String changedFields;
        private String craftGroupId;
        private String description;
        private String extend1;
        private String extend2;
        private String extend3;
        private String maskGroupId;
        private String maskGroupName;
        private String mkuKey;
        private String mkuValues;
        private String recDate;
        private String recStatus;
        private String recUserId;
        private Object referenceCraftGroupId;
        private Object referenceMaskGroupId;
        private String sequenceNBR;
        private String spuId;
        private List<CraftGroupsBean> craftGroups;
        private List<MaskGroupsBean> maskGroups;

        public String getChangedFields() {
            return changedFields;
        }

        public void setChangedFields(String changedFields) {
            this.changedFields = changedFields;
        }

        public String getCraftGroupId() {
            return craftGroupId;
        }

        public void setCraftGroupId(String craftGroupId) {
            this.craftGroupId = craftGroupId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getExtend1() {
            return extend1;
        }

        public void setExtend1(String extend1) {
            this.extend1 = extend1;
        }

        public String getExtend2() {
            return extend2;
        }

        public void setExtend2(String extend2) {
            this.extend2 = extend2;
        }

        public String getExtend3() {
            return extend3;
        }

        public void setExtend3(String extend3) {
            this.extend3 = extend3;
        }

        public String getMaskGroupId() {
            return maskGroupId;
        }

        public void setMaskGroupId(String maskGroupId) {
            this.maskGroupId = maskGroupId;
        }

        public String getMaskGroupName() {
            return maskGroupName;
        }

        public void setMaskGroupName(String maskGroupName) {
            this.maskGroupName = maskGroupName;
        }

        public String getMkuKey() {
            return mkuKey;
        }

        public void setMkuKey(String mkuKey) {
            this.mkuKey = mkuKey;
        }

        public String getMkuValues() {
            return mkuValues;
        }

        public void setMkuValues(String mkuValues) {
            this.mkuValues = mkuValues;
        }

        public String getRecDate() {
            return recDate;
        }

        public void setRecDate(String recDate) {
            this.recDate = recDate;
        }

        public String getRecStatus() {
            return recStatus;
        }

        public void setRecStatus(String recStatus) {
            this.recStatus = recStatus;
        }

        public String getRecUserId() {
            return recUserId;
        }

        public void setRecUserId(String recUserId) {
            this.recUserId = recUserId;
        }

        public Object getReferenceCraftGroupId() {
            return referenceCraftGroupId;
        }

        public void setReferenceCraftGroupId(Object referenceCraftGroupId) {
            this.referenceCraftGroupId = referenceCraftGroupId;
        }

        public Object getReferenceMaskGroupId() {
            return referenceMaskGroupId;
        }

        public void setReferenceMaskGroupId(Object referenceMaskGroupId) {
            this.referenceMaskGroupId = referenceMaskGroupId;
        }

        public String getSequenceNBR() {
            return sequenceNBR;
        }

        public void setSequenceNBR(String sequenceNBR) {
            this.sequenceNBR = sequenceNBR;
        }

        public String getSpuId() {
            return spuId;
        }

        public void setSpuId(String spuId) {
            this.spuId = spuId;
        }

        public List<CraftGroupsBean> getCraftGroups() {
            return craftGroups;
        }

        public void setCraftGroups(List<CraftGroupsBean> craftGroups) {
            this.craftGroups = craftGroups;
        }

        public List<MaskGroupsBean> getMaskGroups() {
            return maskGroups;
        }

        public void setMaskGroups(List<MaskGroupsBean> maskGroups) {
            this.maskGroups = maskGroups;
        }

        public static class CraftGroupsBean implements Serializable {
            /**
             * aspectId : 940516856630992896
             * aspectName : 角度1
             * changedFields :
             * craftGroupId : null
             * craftId : null
             * craftModels : [{"changedFields":"","craftCode":"00006","craftName":"喷绘","description":"喷绘","extend1":"","extend2":"","extend3":"","lockDate":"1512978067000","lockStatus":"N","lockUserId":"908508635489906688","recDate":"1514280327000","recStatus":"A","recUserId":"931419998910464000","sequenceNBR":"940124271571705856","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00011","craftName":"石雕","description":"石雕，指用各种可雕、可刻的石头，创造出具有一定空间的可视、可触的艺术形象，借以反映社会生活、表达艺术家的审美感受、审美情感、审美理想的艺术。常用的石材有花岗石、大理石、青石、砂石等。石材质量坚硬耐风化，是大型纪念性雕塑的主要材料。2008年入选第二批国家级非物质文化遗产名录。","extend1":"","extend2":"","extend3":"","lockDate":"1512982660000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1513585405000","recStatus":"A","recUserId":"931419998910464000","sequenceNBR":"940143537310179328","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00007","craftName":"切割","description":"切割","extend1":"","extend2":"","extend3":"","lockDate":"1512978142000","lockStatus":"N","lockUserId":"908508635489906688","recDate":"1514280538000","recStatus":"A","recUserId":"931419998910464000","sequenceNBR":"940124585662160896","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00010","craftName":"焊接","description":"焊接","extend1":"","extend2":"","extend3":"","lockDate":"1512982510000","lockStatus":"N","lockUserId":"908508635489906688","recDate":"1512982510000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"940142905434087424","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00009","craftName":"印刷","description":"印刷","extend1":"","extend2":"","extend3":"","lockDate":"1512982376000","lockStatus":"N","lockUserId":"908508635489906688","recDate":"1512982376000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"940142342432661504","thumbnail":"940142311805923328_resize.jpg","userName":"","video":""},{"changedFields":"","craftCode":"00008","craftName":"烫金","description":"烫金","extend1":"","extend2":"","extend3":"","lockDate":"1512980206000","lockStatus":"N","lockUserId":"908508635489906688","recDate":"1513309646000","recStatus":"A","recUserId":"909956542105726976","sequenceNBR":"940128550177021952","thumbnail":"undefined","userName":"","video":"940128494640279552.mp4"},{"changedFields":"","craftCode":"00005","craftName":"陶瓷","description":"陶瓷，英语：china。中国人早在约公元前8000－2000年（新石器时代）就发明了陶器。用陶土烧制的器皿叫陶器，用瓷土烧制的器皿叫瓷器。陶瓷则是陶器，炻器和瓷器的总称。古人称陶瓷为瓯。凡是用陶土和瓷土这两种不同性质的粘土为原料，经过配料、成型、干燥、焙烧等工艺流程制成的器物都可以叫陶瓷。","extend1":"","extend2":"","extend3":"","lockDate":"1512977068000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1513585391000","recStatus":"A","recUserId":"931419998910464000","sequenceNBR":"940120081034145792","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00004","craftName":"汝瓷","description":"汝瓷，因产于汝州市故得名汝瓷，为国家地理标志保护产品。汝瓷创烧于北宋晚期，为宫廷御用瓷器。金灭北宋后，汝瓷也随之消亡。其开窑时间前后只有二十年，由于烧造时间短暂，传世亦不多，在南宋时，汝瓷已经非常稀有。现今存世的汝瓷，一般认为有65件，其中台北故宫博物院23件，北京故宫博物院17件，上海博物馆8件，英国戴维基金会（Percival David Foundation of Chinese Art）7件，其他散藏于美、日等博物馆和私人收藏约10件。","extend1":"","extend2":"","extend3":"","lockDate":"1512973591000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1513585381000","recStatus":"A","recUserId":"931419998910464000","sequenceNBR":"940105498793979904","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00003","craftName":"青铜器","description":" 青铜器（Bronze Ware）是由青铜合金（红铜与锡的合金）制成的器具，诞生于人类文明时期的青铜时代。青铜器在世界各地均有出现，是一种世界性文明的象征。最早的青铜器出现于6000年前的古巴比伦两河流域。苏美尔文明时期雕有狮子形象的大型铜刀是早期青铜器的代表。青铜器在2000多年前逐渐由铁器所取代。中国青铜器制作精美，在世界青铜器中享有极高的声誉和艺术价值，代表着中国4000多年青铜发展的高超技术与文化。","extend1":"","extend2":"","extend3":"","lockDate":"1512958573000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1513938982000","recStatus":"A","recUserId":"9527","sequenceNBR":"939446080498622464","thumbnail":"","userName":"","video":""},{"changedFields":"","craftCode":"00002","craftName":"琉璃","description":" 琉璃，琉璃亦作\u201c瑠璃\u201d是用各种颜色（颜色是由各种稀有金属形成）的人造水晶（含24%的二氧化铅）为原料，是在1000多度的高温下烧制而成的。其色彩流云漓彩；其品质晶莹剔透、光彩夺目。中国古代最初制作琉璃的材料，是从青铜器铸造时产生的副产品中获得的，经过提炼加工然后制成琉璃。琉璃的颜色多种多样，古人也叫它\u201c五色石\u201d。古时由于民间很难得到，所以当时人们把琉璃甚至看成比玉器还要珍贵。2008年6月，琉璃烧制技艺入选国务院批准文化部确定的第二批国家级非物质文化遗产名录。目前市场上的琉璃主要是以南方（至台湾）为代表的脱蜡琉璃和以博山（淄博）为代表的手工琉璃为主。国内琉璃制作比较有名的团队分别是琉璃工房、人立琉璃、琉园等 。","extend1":"","extend2":"","extend3":"","lockDate":"1512816327000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1513557342000","recStatus":"A","recUserId":"909956542105726976","sequenceNBR":"939445851586093056","thumbnail":"942673404190814208.jpg","userName":"","video":""},{"changedFields":"","craftCode":"00001","craftName":"苏绣","description":"刺绣是针线在织物上绣制的各种装饰图案的总称。刺绣分丝线刺绣和羽毛刺绣两种。就是用针将丝线或其他纤维、纱线以一定图案和色彩在绣料上穿刺，以绣迹构成花纹的装饰织物。它是用针和线把人的设计和制作添加在任何存在的织物上的一种艺术。刺绣是中国民间传统手工艺之一，在中国至少有二三千年历史。中国刺绣主要有苏绣、湘绣、蜀绣和粤绣四大门类。刺绣的技法有：错针绣、乱针绣、网绣、满地绣、锁丝、纳丝、纳锦、平金、影金、盘金、铺绒、刮绒、戳纱、洒线、挑花等等，刺绣的用途主要包括生活和艺术装饰，如服装、床上用品、台布、舞台、艺术品装饰。","extend1":"","extend2":"","extend3":"","lockDate":"1512816017000","lockStatus":"N","lockUserId":"931419998910464000","recDate":"1514280547000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"939444550458425344","thumbnail":"","userName":"","video":""}]
             * description :
             * extend1 :
             * extend2 :
             * extend3 :
             * mkuId : null
             * recDate : null
             * recStatus :
             * recUserId :
             * sequenceNBR : null
             * spuId : null
             */

            private String aspectId;
            private String aspectName;
            private String changedFields;
            private Object craftGroupId;
            private String craftId;
            private String description;
            private String extend1;
            private String extend2;
            private String extend3;
            private Object mkuId;
            private Object recDate;
            private String recStatus;
            private String recUserId;
            private Object sequenceNBR;
            private Object spuId;
            private List<TechnologyBean> craftModels;

            public String getAspectId() {
                return aspectId;
            }

            public void setAspectId(String aspectId) {
                this.aspectId = aspectId;
            }

            public String getAspectName() {
                return aspectName;
            }

            public void setAspectName(String aspectName) {
                this.aspectName = aspectName;
            }

            public String getChangedFields() {
                return changedFields;
            }

            public void setChangedFields(String changedFields) {
                this.changedFields = changedFields;
            }

            public Object getCraftGroupId() {
                return craftGroupId;
            }

            public void setCraftGroupId(Object craftGroupId) {
                this.craftGroupId = craftGroupId;
            }

            public String getCraftId() {
                return craftId;
            }

            public void setCraftId(String craftId) {
                this.craftId = craftId;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getExtend1() {
                return extend1;
            }

            public void setExtend1(String extend1) {
                this.extend1 = extend1;
            }

            public String getExtend2() {
                return extend2;
            }

            public void setExtend2(String extend2) {
                this.extend2 = extend2;
            }

            public String getExtend3() {
                return extend3;
            }

            public void setExtend3(String extend3) {
                this.extend3 = extend3;
            }

            public Object getMkuId() {
                return mkuId;
            }

            public void setMkuId(Object mkuId) {
                this.mkuId = mkuId;
            }

            public Object getRecDate() {
                return recDate;
            }

            public void setRecDate(Object recDate) {
                this.recDate = recDate;
            }

            public String getRecStatus() {
                return recStatus;
            }

            public void setRecStatus(String recStatus) {
                this.recStatus = recStatus;
            }

            public String getRecUserId() {
                return recUserId;
            }

            public void setRecUserId(String recUserId) {
                this.recUserId = recUserId;
            }

            public Object getSequenceNBR() {
                return sequenceNBR;
            }

            public void setSequenceNBR(Object sequenceNBR) {
                this.sequenceNBR = sequenceNBR;
            }

            public Object getSpuId() {
                return spuId;
            }

            public void setSpuId(Object spuId) {
                this.spuId = spuId;
            }

            public List<TechnologyBean> getCraftModels() {
                return craftModels;
            }

            public void setCraftModels(List<TechnologyBean> craftModels) {
                this.craftModels = craftModels;
            }

            public static class CraftModelsBean implements Serializable {
                /**
                 * changedFields :
                 * craftCode : 00006
                 * craftName : 喷绘
                 * description : 喷绘
                 * extend1 :
                 * extend2 :
                 * extend3 :
                 * lockDate : 1512978067000
                 * lockStatus : N
                 * lockUserId : 908508635489906688
                 * recDate : 1514280327000
                 * recStatus : A
                 * recUserId : 931419998910464000
                 * sequenceNBR : 940124271571705856
                 * thumbnail :
                 * userName :
                 * video :
                 */

                private String changedFields;
                private String craftCode;
                private String craftName;
                private String description;
                private String extend1;
                private String extend2;
                private String extend3;
                private String lockDate;
                private String lockStatus;
                private String lockUserId;
                private String recDate;
                private String recStatus;
                private String recUserId;
                private String sequenceNBR;
                private String thumbnail;
                private String userName;
                private String video;

                public String getChangedFields() {
                    return changedFields;
                }

                public void setChangedFields(String changedFields) {
                    this.changedFields = changedFields;
                }

                public String getCraftCode() {
                    return craftCode;
                }

                public void setCraftCode(String craftCode) {
                    this.craftCode = craftCode;
                }

                public String getCraftName() {
                    return craftName;
                }

                public void setCraftName(String craftName) {
                    this.craftName = craftName;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getExtend1() {
                    return extend1;
                }

                public void setExtend1(String extend1) {
                    this.extend1 = extend1;
                }

                public String getExtend2() {
                    return extend2;
                }

                public void setExtend2(String extend2) {
                    this.extend2 = extend2;
                }

                public String getExtend3() {
                    return extend3;
                }

                public void setExtend3(String extend3) {
                    this.extend3 = extend3;
                }

                public String getLockDate() {
                    return lockDate;
                }

                public void setLockDate(String lockDate) {
                    this.lockDate = lockDate;
                }

                public String getLockStatus() {
                    return lockStatus;
                }

                public void setLockStatus(String lockStatus) {
                    this.lockStatus = lockStatus;
                }

                public String getLockUserId() {
                    return lockUserId;
                }

                public void setLockUserId(String lockUserId) {
                    this.lockUserId = lockUserId;
                }

                public String getRecDate() {
                    return recDate;
                }

                public void setRecDate(String recDate) {
                    this.recDate = recDate;
                }

                public String getRecStatus() {
                    return recStatus;
                }

                public void setRecStatus(String recStatus) {
                    this.recStatus = recStatus;
                }

                public String getRecUserId() {
                    return recUserId;
                }

                public void setRecUserId(String recUserId) {
                    this.recUserId = recUserId;
                }

                public String getSequenceNBR() {
                    return sequenceNBR;
                }

                public void setSequenceNBR(String sequenceNBR) {
                    this.sequenceNBR = sequenceNBR;
                }

                public String getThumbnail() {
                    return thumbnail;
                }

                public void setThumbnail(String thumbnail) {
                    this.thumbnail = thumbnail;
                }

                public String getUserName() {
                    return userName;
                }

                public void setUserName(String userName) {
                    this.userName = userName;
                }

                public String getVideo() {
                    return video;
                }

                public void setVideo(String video) {
                    this.video = video;
                }
            }
        }

        public static class MaskGroupsBean implements Serializable {
            /**
             * aspectId : 940516856630992896
             * aspectName : 角度1
             * changedFields :
             * description :
             * editAreas : [{"border":"940548676621819904_resize.png","changedFields":"","defaultScale":"0.17","description":"","extend1":"","extend2":"","extend3":"","mark":"940548657382547456_resize.png","mask":"940548668220628992_resize.png","maskGroupId":"940516857427910656","maskId":"940548796541112320","minDpi":300,"mkuId":"940516857427910656","position":"{\"x\":234.2857142857143,\"y\":237.14285714285717,\"imgWidth\":1000,\"imgHeight\":1000,\"borderWidth\":\"573\",\"borderHeight\":\"583\"}","recDate":"1513079282000","recStatus":"A","recUserId":"908618890433585152","scale":"0.17","sequenceNBR":"940548796549500928","shade":"940548686839144448_resize.png","spuId":"910759623487700992"}]
             * extend1 :
             * extend2 :
             * extend3 :
             * maskGroupId : 940516857427910656
             * mkuId : 940516857427910656
             * recDate : 1513079282000
             * recStatus : A
             * recUserId : 908618890433585152
             * sequenceNBR : 940548796541112320
             * spuId : 910759623487700992
             * thumbnail : 940548594673508352_resize.jpg
             */

            private String aspectId;
            private String aspectName;
            private String changedFields;
            private String description;
            private String extend1;
            private String extend2;
            private String extend3;
            private String maskGroupId;
            private String mkuId;
            private String recDate;
            private String recStatus;
            private String recUserId;
            private String sequenceNBR;
            private String spuId;
            private String thumbnail;
            private List<EditAreasBean> editAreas;
            public boolean selected;

            public String getAspectId() {
                return aspectId;
            }

            public void setAspectId(String aspectId) {
                this.aspectId = aspectId;
            }

            public String getAspectName() {
                return aspectName;
            }

            public void setAspectName(String aspectName) {
                this.aspectName = aspectName;
            }

            public String getChangedFields() {
                return changedFields;
            }

            public void setChangedFields(String changedFields) {
                this.changedFields = changedFields;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getExtend1() {
                return extend1;
            }

            public void setExtend1(String extend1) {
                this.extend1 = extend1;
            }

            public String getExtend2() {
                return extend2;
            }

            public void setExtend2(String extend2) {
                this.extend2 = extend2;
            }

            public String getExtend3() {
                return extend3;
            }

            public void setExtend3(String extend3) {
                this.extend3 = extend3;
            }

            public String getMaskGroupId() {
                return maskGroupId;
            }

            public void setMaskGroupId(String maskGroupId) {
                this.maskGroupId = maskGroupId;
            }

            public String getMkuId() {
                return mkuId;
            }

            public void setMkuId(String mkuId) {
                this.mkuId = mkuId;
            }

            public String getRecDate() {
                return recDate;
            }

            public void setRecDate(String recDate) {
                this.recDate = recDate;
            }

            public String getRecStatus() {
                return recStatus;
            }

            public void setRecStatus(String recStatus) {
                this.recStatus = recStatus;
            }

            public String getRecUserId() {
                return recUserId;
            }

            public void setRecUserId(String recUserId) {
                this.recUserId = recUserId;
            }

            public String getSequenceNBR() {
                return sequenceNBR;
            }

            public void setSequenceNBR(String sequenceNBR) {
                this.sequenceNBR = sequenceNBR;
            }

            public String getSpuId() {
                return spuId;
            }

            public void setSpuId(String spuId) {
                this.spuId = spuId;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public List<EditAreasBean> getEditAreas() {
                return editAreas;
            }

            public void setEditAreas(List<EditAreasBean> editAreas) {
                this.editAreas = editAreas;
            }

            public static class EditAreasBean implements Serializable {
                /**
                 * border : 940548676621819904_resize.png
                 * changedFields :
                 * defaultScale : 0.17
                 * description :
                 * extend1 :
                 * extend2 :
                 * extend3 :
                 * mark : 940548657382547456_resize.png
                 * mask : 940548668220628992_resize.png
                 * maskGroupId : 940516857427910656
                 * maskId : 940548796541112320
                 * minDpi : 300
                 * mkuId : 940516857427910656
                 * position : {"x":234.2857142857143,"y":237.14285714285717,"imgWidth":1000,"imgHeight":1000,"borderWidth":"573","borderHeight":"583"}
                 * recDate : 1513079282000
                 * recStatus : A
                 * recUserId : 908618890433585152
                 * scale : 0.17
                 * sequenceNBR : 940548796549500928
                 * shade : 940548686839144448_resize.png
                 * spuId : 910759623487700992
                 */

                private String border;
                private String changedFields;
                private String defaultScale;
                private String description;
                private String extend1;
                private String extend2;
                private String extend3;
                private String mark;
                private String mask;
                private String maskGroupId;
                private String maskId;
                private int minDpi;
                private String mkuId;
                private String position;
                private String recDate;
                private String recStatus;
                private String recUserId;
                private String scale;
                private String sequenceNBR;
                private String shade;
                private String spuId;

                public String getBorder() {
                    return border;
                }

                public void setBorder(String border) {
                    this.border = border;
                }

                public String getChangedFields() {
                    return changedFields;
                }

                public void setChangedFields(String changedFields) {
                    this.changedFields = changedFields;
                }

                public String getDefaultScale() {
                    return defaultScale;
                }

                public void setDefaultScale(String defaultScale) {
                    this.defaultScale = defaultScale;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getExtend1() {
                    return extend1;
                }

                public void setExtend1(String extend1) {
                    this.extend1 = extend1;
                }

                public String getExtend2() {
                    return extend2;
                }

                public void setExtend2(String extend2) {
                    this.extend2 = extend2;
                }

                public String getExtend3() {
                    return extend3;
                }

                public void setExtend3(String extend3) {
                    this.extend3 = extend3;
                }

                public String getMark() {
                    return mark;
                }

                public void setMark(String mark) {
                    this.mark = mark;
                }

                public String getMask() {
                    return mask;
                }

                public void setMask(String mask) {
                    this.mask = mask;
                }

                public String getMaskGroupId() {
                    return maskGroupId;
                }

                public void setMaskGroupId(String maskGroupId) {
                    this.maskGroupId = maskGroupId;
                }

                public String getMaskId() {
                    return maskId;
                }

                public void setMaskId(String maskId) {
                    this.maskId = maskId;
                }

                public int getMinDpi() {
                    return minDpi;
                }

                public void setMinDpi(int minDpi) {
                    this.minDpi = minDpi;
                }

                public String getMkuId() {
                    return mkuId;
                }

                public void setMkuId(String mkuId) {
                    this.mkuId = mkuId;
                }

                public String getPosition() {
                    return position;
                }

                public void setPosition(String position) {
                    this.position = position;
                }

                public String getRecDate() {
                    return recDate;
                }

                public void setRecDate(String recDate) {
                    this.recDate = recDate;
                }

                public String getRecStatus() {
                    return recStatus;
                }

                public void setRecStatus(String recStatus) {
                    this.recStatus = recStatus;
                }

                public String getRecUserId() {
                    return recUserId;
                }

                public void setRecUserId(String recUserId) {
                    this.recUserId = recUserId;
                }

                public String getScale() {
                    return scale;
                }

                public void setScale(String scale) {
                    this.scale = scale;
                }

                public String getSequenceNBR() {
                    return sequenceNBR;
                }

                public void setSequenceNBR(String sequenceNBR) {
                    this.sequenceNBR = sequenceNBR;
                }

                public String getShade() {
                    return shade;
                }

                public void setShade(String shade) {
                    this.shade = shade;
                }

                public String getSpuId() {
                    return spuId;
                }

                public void setSpuId(String spuId) {
                    this.spuId = spuId;
                }
            }
        }
    }

    public static class SampleSpuModelBean implements Serializable {
        /**
         * advanced : N
         * aspectNames : []
         * categoryPropertyTypes : []
         * changedFields :
         * component : N
         * customCycle : null
         * deliveryAddress : 陕西西安
         * description : 可定制款陶瓷白盘。
         * designCount : 101
         * designPropertyTypes : []
         * designSpuIds : []
         * designType : 2d
         * extend1 :
         * extend2 :
         * extend3 :
         * extraValue : null
         * face : N
         * isDesignInit :
         * marketPrice : 1
         * marketPriceY : 0.01
         * mkuId : null
         * mkuKey :
         * mkuValues :
         * propertyTypeCodes : []
         * rcDeliveryAddress : 陕西西安
         * recDate : 1513587653000
         * recStatus : A
         * recUserId : 908618890433585152
         * recommendedPrice : 100
         * recommendedPriceY : 1.00
         * sequenceNBR : 910759623487700992
         * skuId : null
         * skus : []
         * spuImages : []
         * spuModel : {"agencyCode":"SUPER_ADMIN","categoryCode":"040223","changedFields":"","commentCount":0,"createDate":"1505976989000","description":"","displayPrice":1,"displayPriceY":"","extend1":"04","extend2":"0402","extend3":"厨具>厨房用具>碗/碟/盘","favoriteCount":1,"hasCollection":false,"lockDate":"1505976989000","lockStatus":"N","lockUserId":"908232819585626112","priority":1,"rcSpuCode":"040223187764100228","rcSpuName":"陶瓷盘子定制款","recDate":"1515210866000","recStatus":"A","recUserId":"9527","salesCount":1,"sequenceNBR":"910759623487700992","spuCode":"910759623449952256-S","spuName":"用来测试2D的抱枕载体载体载体","spuType":"sample","status":"published","supplierId":"909692344465362944","thumbnail":"937620620353589248.png","upvoteCount":0,"viewCount":0}
         * spuPropertyTypes : []
         * spuRichTextImages : []
         * storeNum : null
         * supplierQuote : 1900
         * supplierQuoteY : 19.00
         * tags : []
         */

        private String advanced;
        private String changedFields;
        private String component;
        private Object customCycle;
        private String deliveryAddress;
        private String description;
        private int designCount;
        private String designType;
        private String extend1;
        private String extend2;
        private String extend3;
        private Object extraValue;
        private String face;
        private String isDesignInit;
        private int marketPrice;
        private String marketPriceY;
        private Object mkuId;
        private String mkuKey;
        private String mkuValues;
        private String rcDeliveryAddress;
        private String recDate;
        private String recStatus;
        private String recUserId;
        private int recommendedPrice;
        private String recommendedPriceY;
        private String sequenceNBR;
        private Object skuId;
        private SpuModelBean spuModel;
        private Object storeNum;
        private int supplierQuote;
        private String supplierQuoteY;
        private List<?> aspectNames;
        private List<?> categoryPropertyTypes;
        private List<?> designPropertyTypes;
        private List<?> designSpuIds;
        private List<?> propertyTypeCodes;
        private List<?> skus;
        private List<?> spuImages;
        private List<?> spuPropertyTypes;
        private List<?> spuRichTextImages;
        private List<?> tags;

        public String getAdvanced() {
            return advanced;
        }

        public void setAdvanced(String advanced) {
            this.advanced = advanced;
        }

        public String getChangedFields() {
            return changedFields;
        }

        public void setChangedFields(String changedFields) {
            this.changedFields = changedFields;
        }

        public String getComponent() {
            return component;
        }

        public void setComponent(String component) {
            this.component = component;
        }

        public Object getCustomCycle() {
            return customCycle;
        }

        public void setCustomCycle(Object customCycle) {
            this.customCycle = customCycle;
        }

        public String getDeliveryAddress() {
            return deliveryAddress;
        }

        public void setDeliveryAddress(String deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getDesignCount() {
            return designCount;
        }

        public void setDesignCount(int designCount) {
            this.designCount = designCount;
        }

        public String getDesignType() {
            return designType;
        }

        public void setDesignType(String designType) {
            this.designType = designType;
        }

        public String getExtend1() {
            return extend1;
        }

        public void setExtend1(String extend1) {
            this.extend1 = extend1;
        }

        public String getExtend2() {
            return extend2;
        }

        public void setExtend2(String extend2) {
            this.extend2 = extend2;
        }

        public String getExtend3() {
            return extend3;
        }

        public void setExtend3(String extend3) {
            this.extend3 = extend3;
        }

        public Object getExtraValue() {
            return extraValue;
        }

        public void setExtraValue(Object extraValue) {
            this.extraValue = extraValue;
        }

        public String getFace() {
            return face;
        }

        public void setFace(String face) {
            this.face = face;
        }

        public String getIsDesignInit() {
            return isDesignInit;
        }

        public void setIsDesignInit(String isDesignInit) {
            this.isDesignInit = isDesignInit;
        }

        public int getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(int marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getMarketPriceY() {
            return marketPriceY;
        }

        public void setMarketPriceY(String marketPriceY) {
            this.marketPriceY = marketPriceY;
        }

        public Object getMkuId() {
            return mkuId;
        }

        public void setMkuId(Object mkuId) {
            this.mkuId = mkuId;
        }

        public String getMkuKey() {
            return mkuKey;
        }

        public void setMkuKey(String mkuKey) {
            this.mkuKey = mkuKey;
        }

        public String getMkuValues() {
            return mkuValues;
        }

        public void setMkuValues(String mkuValues) {
            this.mkuValues = mkuValues;
        }

        public String getRcDeliveryAddress() {
            return rcDeliveryAddress;
        }

        public void setRcDeliveryAddress(String rcDeliveryAddress) {
            this.rcDeliveryAddress = rcDeliveryAddress;
        }

        public String getRecDate() {
            return recDate;
        }

        public void setRecDate(String recDate) {
            this.recDate = recDate;
        }

        public String getRecStatus() {
            return recStatus;
        }

        public void setRecStatus(String recStatus) {
            this.recStatus = recStatus;
        }

        public String getRecUserId() {
            return recUserId;
        }

        public void setRecUserId(String recUserId) {
            this.recUserId = recUserId;
        }

        public int getRecommendedPrice() {
            return recommendedPrice;
        }

        public void setRecommendedPrice(int recommendedPrice) {
            this.recommendedPrice = recommendedPrice;
        }

        public String getRecommendedPriceY() {
            return recommendedPriceY;
        }

        public void setRecommendedPriceY(String recommendedPriceY) {
            this.recommendedPriceY = recommendedPriceY;
        }

        public String getSequenceNBR() {
            return sequenceNBR;
        }

        public void setSequenceNBR(String sequenceNBR) {
            this.sequenceNBR = sequenceNBR;
        }

        public Object getSkuId() {
            return skuId;
        }

        public void setSkuId(Object skuId) {
            this.skuId = skuId;
        }

        public SpuModelBean getSpuModel() {
            return spuModel;
        }

        public void setSpuModel(SpuModelBean spuModel) {
            this.spuModel = spuModel;
        }

        public Object getStoreNum() {
            return storeNum;
        }

        public void setStoreNum(Object storeNum) {
            this.storeNum = storeNum;
        }

        public int getSupplierQuote() {
            return supplierQuote;
        }

        public void setSupplierQuote(int supplierQuote) {
            this.supplierQuote = supplierQuote;
        }

        public String getSupplierQuoteY() {
            return supplierQuoteY;
        }

        public void setSupplierQuoteY(String supplierQuoteY) {
            this.supplierQuoteY = supplierQuoteY;
        }

        public List<?> getAspectNames() {
            return aspectNames;
        }

        public void setAspectNames(List<?> aspectNames) {
            this.aspectNames = aspectNames;
        }

        public List<?> getCategoryPropertyTypes() {
            return categoryPropertyTypes;
        }

        public void setCategoryPropertyTypes(List<?> categoryPropertyTypes) {
            this.categoryPropertyTypes = categoryPropertyTypes;
        }

        public List<?> getDesignPropertyTypes() {
            return designPropertyTypes;
        }

        public void setDesignPropertyTypes(List<?> designPropertyTypes) {
            this.designPropertyTypes = designPropertyTypes;
        }

        public List<?> getDesignSpuIds() {
            return designSpuIds;
        }

        public void setDesignSpuIds(List<?> designSpuIds) {
            this.designSpuIds = designSpuIds;
        }

        public List<?> getPropertyTypeCodes() {
            return propertyTypeCodes;
        }

        public void setPropertyTypeCodes(List<?> propertyTypeCodes) {
            this.propertyTypeCodes = propertyTypeCodes;
        }

        public List<?> getSkus() {
            return skus;
        }

        public void setSkus(List<?> skus) {
            this.skus = skus;
        }

        public List<?> getSpuImages() {
            return spuImages;
        }

        public void setSpuImages(List<?> spuImages) {
            this.spuImages = spuImages;
        }

        public List<?> getSpuPropertyTypes() {
            return spuPropertyTypes;
        }

        public void setSpuPropertyTypes(List<?> spuPropertyTypes) {
            this.spuPropertyTypes = spuPropertyTypes;
        }

        public List<?> getSpuRichTextImages() {
            return spuRichTextImages;
        }

        public void setSpuRichTextImages(List<?> spuRichTextImages) {
            this.spuRichTextImages = spuRichTextImages;
        }

        public List<?> getTags() {
            return tags;
        }

        public void setTags(List<?> tags) {
            this.tags = tags;
        }

        public static class SpuModelBean implements Serializable {
            /**
             * agencyCode : SUPER_ADMIN
             * categoryCode : 040223
             * changedFields :
             * commentCount : 0
             * createDate : 1505976989000
             * description :
             * displayPrice : 1
             * displayPriceY :
             * extend1 : 04
             * extend2 : 0402
             * extend3 : 厨具>厨房用具>碗/碟/盘
             * favoriteCount : 1
             * hasCollection : false
             * lockDate : 1505976989000
             * lockStatus : N
             * lockUserId : 908232819585626112
             * priority : 1
             * rcSpuCode : 040223187764100228
             * rcSpuName : 陶瓷盘子定制款
             * recDate : 1515210866000
             * recStatus : A
             * recUserId : 9527
             * salesCount : 1
             * sequenceNBR : 910759623487700992
             * spuCode : 910759623449952256-S
             * spuName : 用来测试2D的抱枕载体载体载体
             * spuType : sample
             * status : published
             * supplierId : 909692344465362944
             * thumbnail : 937620620353589248.png
             * upvoteCount : 0
             * viewCount : 0
             */

            private String agencyCode;
            private String categoryCode;
            private String changedFields;
            private int commentCount;
            private String createDate;
            private String description;
            private int displayPrice;
            private String displayPriceY;
            private String extend1;
            private String extend2;
            private String extend3;
            private int favoriteCount;
            private boolean hasCollection;
            private String lockDate;
            private String lockStatus;
            private String lockUserId;
            private int priority;
            private String rcSpuCode;
            private String rcSpuName;
            private String recDate;
            private String recStatus;
            private String recUserId;
            private int salesCount;
            private String sequenceNBR;
            private String spuCode;
            private String spuName;
            private String spuType;
            private String status;
            private String supplierId;
            private String thumbnail;
            private int upvoteCount;
            private int viewCount;

            public String getAgencyCode() {
                return agencyCode;
            }

            public void setAgencyCode(String agencyCode) {
                this.agencyCode = agencyCode;
            }

            public String getCategoryCode() {
                return categoryCode;
            }

            public void setCategoryCode(String categoryCode) {
                this.categoryCode = categoryCode;
            }

            public String getChangedFields() {
                return changedFields;
            }

            public void setChangedFields(String changedFields) {
                this.changedFields = changedFields;
            }

            public int getCommentCount() {
                return commentCount;
            }

            public void setCommentCount(int commentCount) {
                this.commentCount = commentCount;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getDisplayPrice() {
                return displayPrice;
            }

            public void setDisplayPrice(int displayPrice) {
                this.displayPrice = displayPrice;
            }

            public String getDisplayPriceY() {
                return displayPriceY;
            }

            public void setDisplayPriceY(String displayPriceY) {
                this.displayPriceY = displayPriceY;
            }

            public String getExtend1() {
                return extend1;
            }

            public void setExtend1(String extend1) {
                this.extend1 = extend1;
            }

            public String getExtend2() {
                return extend2;
            }

            public void setExtend2(String extend2) {
                this.extend2 = extend2;
            }

            public String getExtend3() {
                return extend3;
            }

            public void setExtend3(String extend3) {
                this.extend3 = extend3;
            }

            public int getFavoriteCount() {
                return favoriteCount;
            }

            public void setFavoriteCount(int favoriteCount) {
                this.favoriteCount = favoriteCount;
            }

            public boolean isHasCollection() {
                return hasCollection;
            }

            public void setHasCollection(boolean hasCollection) {
                this.hasCollection = hasCollection;
            }

            public String getLockDate() {
                return lockDate;
            }

            public void setLockDate(String lockDate) {
                this.lockDate = lockDate;
            }

            public String getLockStatus() {
                return lockStatus;
            }

            public void setLockStatus(String lockStatus) {
                this.lockStatus = lockStatus;
            }

            public String getLockUserId() {
                return lockUserId;
            }

            public void setLockUserId(String lockUserId) {
                this.lockUserId = lockUserId;
            }

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
            }

            public String getRcSpuCode() {
                return rcSpuCode;
            }

            public void setRcSpuCode(String rcSpuCode) {
                this.rcSpuCode = rcSpuCode;
            }

            public String getRcSpuName() {
                return rcSpuName;
            }

            public void setRcSpuName(String rcSpuName) {
                this.rcSpuName = rcSpuName;
            }

            public String getRecDate() {
                return recDate;
            }

            public void setRecDate(String recDate) {
                this.recDate = recDate;
            }

            public String getRecStatus() {
                return recStatus;
            }

            public void setRecStatus(String recStatus) {
                this.recStatus = recStatus;
            }

            public String getRecUserId() {
                return recUserId;
            }

            public void setRecUserId(String recUserId) {
                this.recUserId = recUserId;
            }

            public int getSalesCount() {
                return salesCount;
            }

            public void setSalesCount(int salesCount) {
                this.salesCount = salesCount;
            }

            public String getSequenceNBR() {
                return sequenceNBR;
            }

            public void setSequenceNBR(String sequenceNBR) {
                this.sequenceNBR = sequenceNBR;
            }

            public String getSpuCode() {
                return spuCode;
            }

            public void setSpuCode(String spuCode) {
                this.spuCode = spuCode;
            }

            public String getSpuName() {
                return spuName;
            }

            public void setSpuName(String spuName) {
                this.spuName = spuName;
            }

            public String getSpuType() {
                return spuType;
            }

            public void setSpuType(String spuType) {
                this.spuType = spuType;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getSupplierId() {
                return supplierId;
            }

            public void setSupplierId(String supplierId) {
                this.supplierId = supplierId;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public int getUpvoteCount() {
                return upvoteCount;
            }

            public void setUpvoteCount(int upvoteCount) {
                this.upvoteCount = upvoteCount;
            }

            public int getViewCount() {
                return viewCount;
            }

            public void setViewCount(int viewCount) {
                this.viewCount = viewCount;
            }
        }
    }
}
