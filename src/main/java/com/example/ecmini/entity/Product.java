package com.example.ecmini.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

//DBのテーブルですよ
@Entity
@Data

public class Product {

    //主キー
    @Id
    //自動採番
    //フィールドは基本private。外部から書き換えを防ぐ
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String author;
    private Integer price;
    private String category;
    private String image;
    private Integer stock;

    //descripitonの文字数を長めにする
    @Column(length = 2000)
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //登録時・更新時に日時を自動セット
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // @Dataがgetter / setterを全部自動生成

    public void setId(Long id) {
        this.id = id;
    }


}
