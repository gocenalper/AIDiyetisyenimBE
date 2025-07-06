package org.example.openai;

import org.example.model.DietRequest;

import java.util.StringJoiner;

public class PromptBuilder {

    public static String buildPrompt(DietRequest request) {
        String liked = listToString(request.getLikedFoods());
        String disliked = listToString(request.getDislikedFoods());
        String allergic = listToString(request.getAllergicFoods());

        return """
    Sen bir diyetisyensin. Aşağıdaki kullanıcı bilgilerine göre, sadece geçerli JSON olacak şekilde bir haftalık diyet listesi üret.

    Kurallar:
    1. 7 gün boyunca her gün için 5 öğün içeren diyet oluştur:
       - BREAKFAST
       - MORNING_SNACK
       - LUNCH
       - AFTERNOON_SNACK
       - DINNER
    2. Her öğünde 2 ila 4 gıda yer almalı.
    3. Her gıda şu formatta tanımlanmalı:
       - name (örnek: "Yulaf ezmesi")
       - amount (örnek: 50)
       - unit (seçenekler: "GR", "MG", "ADET", "ML", "DILIM")
       - calories (örnek: 180)
    4. Gün adı `day` alanında MONDAY, TUESDAY, ... şeklinde İngilizce büyük harflerle belirtilmeli.
    5. mealType alanı sadece şu 5 değerden biri olmalı: BREAKFAST, MORNING_SNACK, LUNCH, AFTERNOON_SNACK, DINNER
    6. Yanıt sadece geçerli JSON içermeli. Açıklama, yorum, markdown kod bloğu (örneğin ```json) gibi ifadeler **asla** yer almamalı.

    Ek kriterler:
    - Gıdalar Türkiye'de yaygın ve kolay bulunabilir olmalı.
    - Maliyet orta düzey olmalı.
    - Aynı besin 7 gün içinde maksimum 2 kez kullanılmalı.
    - Yemekler pratik, günlük hayata uygun ve gerçekçi olmalı.

    Kullanıcı bilgileri:
    - Boy: %.0f cm
    - Kilo: %.1f kg
    - Hedef kilo: %.1f kg
    - Aktivite seviyesi: %s
    - Sevdiği yiyecekler: %s
    - Sevmediği yiyecekler: %s
    - Alerjik olduğu yiyecekler: %s

    JSON örnek yapısı sadece aşağıdaki gibi olmalı:

    [
      {
        "day": "MONDAY",
        "meals": [
          {
            "mealType": "BREAKFAST",
            "foods": [
              {
                "name": "Yulaf ezmesi",
                "amount": 50,
                "unit": "gram",
                "calories": 180
              }
            ]
          }
        ]
      }
    ]
    """.formatted(
                request.getHeight(),
                request.getWeight(),
                request.getTargetWeight(),
                request.getActivityLevel(),
                liked,
                disliked,
                allergic
        );
    }


    private static String listToString(java.util.List<String> list) {
        if (list == null || list.isEmpty()) return "-";
        return String.join(", ", list);
    }
}


