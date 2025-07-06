package org.example.openai;

import org.example.model.DietRequest;

import java.util.StringJoiner;

public class PromptBuilder {

    public static String buildPrompt(DietRequest request) {
        String liked = listToString(request.getLikedFoods());
        String disliked = listToString(request.getDislikedFoods());
        String allergic = listToString(request.getAllergicFoods());

        return """
        Sen bir diyetisyensin. Aşağıda belirtilen kullanıcının bilgilerinden yola çıkarak, haftalık bir diyet listesi oluşturmanı istiyorum.

        Kurallar:
        1. 7 gün boyunca her gün için ayrı diyet oluştur.
        2. Her gün için 5 öğün ver: 
           - Kahvaltı (BREAKFAST)
           - Sabah Ara Öğünü (MORNING_SNACK)
           - Öğle Yemeği (LUNCH)
           - Öğleden Sonra Ara Öğün (AFTERNOON_SNACK)
           - Akşam Yemeği (DINNER)
        3. Her öğünde 2-4 farklı besin olsun.
        4. Her besin için aşağıdaki bilgiler ayrı ayrı verilmelidir:
           - name: Besinin adı (örneğin "Yulaf ezmesi")
           - amount: Sayı cinsinden miktar (örneğin 50)
           - unit: Birim (örneğin "gram", "adet", "kase", "dilim", "yemek kaşığı", "bardak")
           - calories: Kalori değeri (örneğin 180)
        5. day alanı İngilizce büyük harflerle olsun: MONDAY, TUESDAY, ..., SUNDAY
        6. mealType alanı aşağıdaki 5 değerden biri olsun:
           - BREAKFAST
           - MORNING_SNACK
           - LUNCH
           - AFTERNOON_SNACK
           - DINNER

        JSON örnek yapısı yalnızca aşağıdaki gibi olmalıdır:

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
                    "calories": 190
                  }
                ]
              }
            ]
          }
        ]

        Kullanıcı bilgileri:
        - Boy: %.0f cm
        - Kilo: %.1f kg
        - Hedef kilo: %.1f kg
        - Aktivite seviyesi: %s
        - Sevdiği yiyecekler: %s
        - Sevmediği yiyecekler: %s
        - Alerjik olduğu besinler: %s

        Lütfen yalnızca yukarıdaki JSON formatında yanıt ver. Açıklama, yorum veya metin ekleme. Yanıt sadece geçerli JSON olmalı.
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
        StringJoiner joiner = new StringJoiner(", ");
        list.forEach(joiner::add);
        return joiner.toString();
    }
}


