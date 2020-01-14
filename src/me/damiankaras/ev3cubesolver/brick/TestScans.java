package me.damiankaras.ev3cubesolver.brick;

public class TestScans {

    static float SCRAMLED[][] =
            {{0.30921569f,0.35490197f,0.24901961f},
            {0.03529412f,0.21176471f,0.12352941f},
            {0.03137255f,0.19509804f,0.11470588f},
            {0.25000000f,0.26176471f,0.16568628f},
            {0.03431373f,0.19803922f,0.11764706f},
            {0.02352941f,0.05784314f,0.06862745f},
            {0.29215688f,0.30294117f,0.18725491f},
            {0.24901961f,0.28921568f,0.19607843f},
            {0.28921568f,0.24117647f,0.24803922f},
            {0.04470588f,0.24215686f,0.14803922f},
            {0.27843139f,0.23137255f,0.24313726f},
            {0.25882354f,0.29411766f,0.20196079f},
            {0.25588235f,0.26764706f,0.16862746f},
            {0.26274511f,0.30098039f,0.20294118f},
            {0.25392157f,0.21372549f,0.22647059f},
            {0.28039217f,0.29509804f,0.18137255f},
            {0.23921569f,0.27941176f,0.18823530f},
            {0.28039217f,0.29019609f,0.18235295f},
            {0.34460786f,0.35098040f,0.22549020f},
            {0.26274511f,0.30392158f,0.20588236f},
            {0.27647060f,0.29019609f,0.18039216f},
            {0.13627452f,0.03137255f,0.01568628f},
            {0.02549020f,0.05686275f,0.07156863f},
            {0.25588235f,0.27254903f,0.16862746f},
            {0.03333334f,0.21176471f,0.12058824f},
            {0.24215686f,0.27843139f,0.19215687f},
            {0.02647059f,0.06176471f,0.07352941f},
            {0.03259804f,0.07058824f,0.09215686f},
            {0.28333333f,0.23333333f,0.24705882f},
            {0.25686276f,0.29117647f,0.20196079f},
            {0.02254902f,0.05098039f,0.06666667f},
            {0.03137255f,0.19509804f,0.11372549f},
            {0.14411765f,0.03725490f,0.01568628f},
            {0.15980393f,0.03823530f,0.01960784f},
            {0.03137255f,0.19411765f,0.11176471f},
            {0.02647059f,0.06078431f,0.07450981f},
            {0.18906863f,0.04215686f,0.02352941f},
            {0.03333334f,0.20098040f,0.11568628f},
            {0.26372549f,0.21960784f,0.23529412f},
            {0.02450980f,0.05686275f,0.06764706f},
            {0.27156863f,0.22549020f,0.23823529f},
            {0.26470590f,0.27647060f,0.17254902f},
            {0.15294118f,0.03529412f,0.01764706f},
            {0.14019608f,0.03333334f,0.01470588f},
            {0.26078433f,0.21666667f,0.23235294f},
            {0.33808821f,0.27450982f,0.29901960f},
            {0.14509805f,0.03529412f,0.01862745f},
            {0.24411765f,0.28235295f,0.19313726f},
            {0.23529412f,0.19901961f,0.21568628f},
            {0.15294118f,0.03529412f,0.01764706f},
            {0.02352941f,0.05588235f,0.06862745f},
            {0.02352941f,0.05588235f,0.06960785f},
            {0.02941176f,0.18823530f,0.11176471f},
            {0.14215687f,0.03235294f,0.01862745f}};

    static float SOLVED[][] =
            {{0.30921569f,0.35686275f,0.24509804f},
            {0.27058825f,0.31274509f,0.20686275f},
            {0.25882354f,0.29509804f,0.20000000f},
            {0.23529412f,0.27156863f,0.18529412f},
            {0.24901961f,0.28333333f,0.19705883f},
            {0.21862745f,0.25392157f,0.17745098f},
            {0.24313726f,0.27549019f,0.19607843f},
            {0.23725490f,0.27058825f,0.18921569f},
            {0.26666668f,0.30196080f,0.20588236f},
            {0.34088236f,0.27745098f,0.29901960f},
            {0.28431374f,0.23627451f,0.24607843f},
            {0.28823531f,0.23627451f,0.24901961f},
            {0.24215686f,0.20392157f,0.21862745f},
            {0.25000000f,0.20588236f,0.23333333f},
            {0.23431373f,0.19803922f,0.21274510f},
            {0.23235294f,0.19803922f,0.22254902f},
            {0.25588235f,0.21470588f,0.22745098f},
            {0.25980392f,0.21764706f,0.23823529f},
            {0.34460786f,0.35392156f,0.22352941f},
            {0.27941176f,0.29607844f,0.18137255f},
            {0.27549019f,0.28333333f,0.18039216f},
            {0.25000000f,0.26274511f,0.16470589f},
            {0.25980392f,0.26666668f,0.17647059f},
            {0.23431373f,0.25000000f,0.15784314f},
            {0.25980392f,0.26568627f,0.17549020f},
            {0.25686276f,0.27352941f,0.16862746f},
            {0.27352941f,0.28235295f,0.18039216f},
            {0.19000000f,0.04411765f,0.02450980f},
            {0.16078432f,0.03921569f,0.01960784f},
            {0.15882353f,0.03823530f,0.01764706f},
            {0.14215687f,0.03235294f,0.01666667f},
            {0.14411765f,0.03431373f,0.01764706f},
            {0.13137256f,0.03235294f,0.01568628f},
            {0.14215687f,0.03627451f,0.01764706f},
            {0.14019608f,0.03333334f,0.01568628f},
            {0.15392157f,0.03627451f,0.01666667f},
            {0.04191177f,0.24019608f,0.14705883f},
            {0.03333334f,0.20784314f,0.11960784f},
            {0.03235294f,0.19901961f,0.11666667f},
            {0.02745098f,0.18137255f,0.10490196f},
            {0.03137255f,0.19019608f,0.11372549f},
            {0.02843137f,0.17450981f,0.10000000f},
            {0.03137255f,0.17352942f,0.10882353f},
            {0.03039216f,0.18137255f,0.10686275f},
            {0.03235294f,0.18529412f,0.11666667f},
            {0.03073530f,0.06862745f,0.09019608f},
            {0.02549020f,0.05882353f,0.07254902f},
            {0.02450980f,0.05882353f,0.07254902f},
            {0.02156863f,0.05294118f,0.06568628f},
            {0.02549020f,0.05588235f,0.06960785f},
            {0.02450980f,0.05588235f,0.06568628f},
            {0.02549020f,0.05882353f,0.07352941f},
            {0.02352941f,0.05686275f,0.06960785f},
            {0.02450980f,0.05686275f,0.07254902f}};

    static float PARTIALLY_SCRAMBLED[][] =
            {{0.31107843f,0.35980392f,0.25000000f},
            {0.15294118f,0.03725490f,0.01862745f},
            {0.13823530f,0.03235294f,0.01666667f},
            {0.22941177f,0.27254903f,0.18235295f},
            {0.27647060f,0.22941177f,0.24019608f},
            {0.24607843f,0.20294118f,0.22058824f},
            {0.28529412f,0.23529412f,0.25000000f},
            {0.24019608f,0.27254903f,0.19313726f},
            {0.14215687f,0.03333334f,0.01862745f},
            {0.04191177f,0.24607843f,0.14901961f},
            {0.03333334f,0.20294118f,0.11568628f},
            {0.03235294f,0.19705883f,0.11372549f},
            {0.03235294f,0.19019608f,0.10882353f},
            {0.03333334f,0.20294118f,0.11666667f},
            {0.02941176f,0.18725491f,0.10980392f},
            {0.03333334f,0.20294118f,0.11862745f},
            {0.03137255f,0.18823530f,0.11176471f},
            {0.03137255f,0.19607843f,0.11470588f},
            {0.34367645f,0.34999999f,0.22647059f},
            {0.14901961f,0.03333334f,0.01764706f},
            {0.14509805f,0.03235294f,0.01568628f},
            {0.26176471f,0.27352941f,0.17156863f},
            {0.26960784f,0.22450981f,0.23529412f},
            {0.26176471f,0.21764706f,0.23039216f},
            {0.27352941f,0.22843137f,0.23921569f},
            {0.25784314f,0.27156863f,0.16862746f},
            {0.14705883f,0.03235294f,0.01666667f},
            {0.03166667f,0.06960785f,0.09117647f},
            {0.02352941f,0.05686275f,0.07058824f},
            {0.02549020f,0.05490196f,0.07058824f},
            {0.02549020f,0.06078431f,0.06960785f},
            {0.02647059f,0.06176471f,0.07254902f},
            {0.02549020f,0.05882353f,0.06862745f},
            {0.02549020f,0.05882353f,0.07352941f},
            {0.02352941f,0.05294118f,0.06666667f},
            {0.02450980f,0.05588235f,0.07156863f},
            {0.17789216f,0.04117647f,0.02450980f},
            {0.27843139f,0.29411766f,0.18137255f},
            {0.28725490f,0.29803923f,0.18627451f},
            {0.14411765f,0.03333334f,0.01666667f},
            {0.24705882f,0.28529412f,0.19215687f},
            {0.23921569f,0.27941176f,0.18921569f},
            {0.24411765f,0.28333333f,0.18921569f},
            {0.13333334f,0.03235294f,0.01666667f},
            {0.25980392f,0.26960784f,0.16960785f},
            {0.32411766f,0.25980392f,0.29509804f},
            {0.27843139f,0.29607844f,0.18137255f},
            {0.28137255f,0.29117647f,0.18333334f},
            {0.25686276f,0.21274510f,0.23235294f},
            {0.25196078f,0.29019609f,0.19509804f},
            {0.23823529f,0.27941176f,0.18823530f},
            {0.25196078f,0.29509804f,0.19509804f},
            {0.23823529f,0.20098040f,0.21372549f},
            {0.26666668f,0.27745098f,0.17352942f}};


    static float INORRECT_SCAN[][] = {
            {0.03529412f,0.07058824f,0.08725490f},
            {0.26078433f,0.27843139f,0.16862746f},
            {0.02549020f,0.05882353f,0.06862745f},
            {0.02058824f,0.04901961f,0.06176471f},
            {0.24411765f,0.28137255f,0.18823530f},
            {0.20980392f,0.23921569f,0.17058824f},
            {0.02941176f,0.17450981f,0.10784314f},
            {0.02843137f,0.15980393f,0.09705883f},
            {0.24117647f,0.25588235f,0.15784314f},
            {0.17745098f,0.04509804f,0.02450980f},
            {0.23725490f,0.27254903f,0.18431373f},
            {0.24803922f,0.20588236f,0.22647059f},
            {0.22156863f,0.18627451f,0.21078432f},
            {0.20294118f,0.23725490f,0.16764706f},
            {0.12549020f,0.03333334f,0.01372549f},
            {0.13431373f,0.03235294f,0.01764706f},
            {0.12941177f,0.03039216f,0.01568628f},
            {0.23431373f,0.26862746f,0.18333334f},
            {0.04215686f,0.21960784f,0.13823530f},
            {0.03235294f,0.19117647f,0.11078431f},
            {0.23039216f,0.26568627f,0.18627451f},
            {0.02450980f,0.05000000f,0.06470589f},
            {0.02647059f,0.05392157f,0.06078431f},
            {0.02843137f,0.16470589f,0.09705883f},
            {0.03137255f,0.17843138f,0.10392157f},
            {0.03039216f,0.17647059f,0.10196079f},
            {0.03431373f,0.19215687f,0.11274510f},
            {0.32254902f,0.25294119f,0.27941176f},
            {0.14019608f,0.03431373f,0.01568628f},
            {0.14411765f,0.04019608f,0.01862745f},
            {0.21960784f,0.18529412f,0.20980392f},
            {0.02352941f,0.05196078f,0.06372549f},
            {0.21764706f,0.23039216f,0.14607844f},
            {0.24117647f,0.25588235f,0.15686275f},
            {0.22647059f,0.19313726f,0.20588236f},
            {0.24509804f,0.25588235f,0.16078432f},
            {0.33333334f,0.33529413f,0.21274510f},
            {0.02647059f,0.06078431f,0.06666667f},
            {0.02647059f,0.06176471f,0.06568628f},
            {0.22941177f,0.24901961f,0.14901961f},
            {0.24019608f,0.20294118f,0.21274510f},
            {0.23333333f,0.24411765f,0.15588236f},
            {0.03137255f,0.18431373f,0.10980392f},
            {0.21862745f,0.24803922f,0.17352942f},
            {0.13137256f,0.03529412f,0.01666667f},
            {0.31470588f,0.34803921f,0.23823529f},
            {0.24313726f,0.28823531f,0.18235295f},
            {0.24509804f,0.20588236f,0.21372549f},
            {0.23431373f,0.19509804f,0.21078432f},
            {0.23333333f,0.19411765f,0.21078432f},
            {0.02058824f,0.05098039f,0.06078431f},
            {0.02549020f,0.05588235f,0.06470589f},
            {0.13039216f,0.03137255f,0.01372549f},
            {0.13921569f,0.03823530f,0.01764706f}};

}
