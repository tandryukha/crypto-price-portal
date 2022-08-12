package com.kuehnenagel.crypto.price.api.bitcoin;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Price {

    @SerializedName(value = "rate_float")
    private Double rateFloat;
}
