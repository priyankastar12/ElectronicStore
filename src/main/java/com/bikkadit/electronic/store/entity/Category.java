package com.bikkadit.electronic.store.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="categories")
public class Category {
       @Id
    private String categoryId;
       @Column(name = "category_title",length = 60,nullable = false)
    private String title;
       @Column(name = "category_desc",length =500)
    private String description;
    private String coverImage;




}
