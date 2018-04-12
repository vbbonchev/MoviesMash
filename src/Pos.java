

import java.util.Map;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import edu.mit.jwi.item.POS;

public enum Pos {

  NOUN, VERB, ADJECTIVE, ADVERB, OTHER;

  private static Map<String,Pos> bmap = null;
  private static BidiMap<Pos,POS> wmap = null;
  private static final String translationFile = 
    "src/main/resources/brown_tags.txt";


  public static Pos fromWordnetPOS(POS pos) {
    if (wmap == null) {
      wmap = buildPosBidiMap();
    }
    return wmap.getKey(pos);
  }
  
  public static POS toWordnetPos(Pos pos) {
    if (wmap == null) {
      wmap = buildPosBidiMap();
    }
    return wmap.get(pos);
  }

  private static BidiMap<Pos,POS> buildPosBidiMap() {
    wmap = new DualHashBidiMap<Pos,POS>();
    wmap.put(Pos.NOUN, POS.NOUN);
    wmap.put(Pos.VERB, POS.VERB);
    wmap.put(Pos.ADJECTIVE, POS.ADJECTIVE);
    wmap.put(Pos.ADVERB, POS.ADVERB);
    wmap.put(Pos.OTHER, null);
    return wmap;
  }
}
