package com.atziluth.sephirah.chesed.util;

import com.atziluth.sephirah.chesed.model.*;
import com.atziluth.sephirah.chesed.factory.UmaFactory;
import java.util.*;

/**
 * Utility class for generating test data.
 */
public class DataGenerator {
    
    private static final String[] NAMES = {
        "Special Week", "Silence Suzuka", "Tokai Teio", "Oguri Cap", "Gold Ship",
        "Vodka", "Daiwa Scarlet", "Mejiro Mcqueen", "Symboli Rudolf", "Sakura Bakushin O"
    };
    
    private static final String[] JAPANESE_NAMES = {
        "スペシャルウィーク", "サイレンススズカ", "トウカイテイオー", "オグリキャップ", "ゴールドシップ",
        "ウオッカ", "ダイワスカーレット", "メジロマックイーン", "シンボリルドルフ", "サクラバクシンオー"
    };
    
    private static final Umamusume.Rarity[] RARITIES = {
        Umamusume.Rarity.UR, Umamusume.Rarity.UR, Umamusume.Rarity.SSR,
        Umamusume.Rarity.SSR, Umamusume.Rarity.SR, Umamusume.Rarity.SR,
        Umamusume.Rarity.R, Umamusume.Rarity.R, Umamusume.Rarity.R, Umamusume.Rarity.N
    };
    
    public static List<AbstractUma> generateMockCharacters(int count) {
        List<AbstractUma> characters = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 0; i < Math.min(count, NAMES.length); i++) {
            UmaFactory.UmaType type = UmaFactory.UmaType.values()[random.nextInt(5)];
            AbstractUma uma = UmaFactory.createUma(type, NAMES[i], JAPANESE_NAMES[i], RARITIES[i]);
            uma.setId(1001 + i);
            characters.add(uma);
        }
        
        return characters;
    }
    
    public static Umamusume generateMockUmamusume() {
        Random random = new Random();
        int idx = random.nextInt(NAMES.length);
        
        Umamusume uma = new Umamusume(NAMES[idx], RARITIES[idx], 
                                     Umamusume.CharacterType.values()[random.nextInt(5)]);
        uma.setId(1001 + idx);
        uma.setJapaneseName(JAPANESE_NAMES[idx]);
        
        Umamusume.Stats stats = new Umamusume.Stats(
            70 + random.nextInt(30),
            70 + random.nextInt(30),
            70 + random.nextInt(30),
            70 + random.nextInt(30),
            70 + random.nextInt(30)
        );
        uma.setStats(stats);
        
        return uma;
    }
    
    public static List<Umamusume> generateMockUmamususeList(int count) {
        List<Umamusume> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(generateMockUmamusume());
        }
        return list;
    }
}
