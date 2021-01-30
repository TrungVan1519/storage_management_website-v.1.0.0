package com.trungvan.entity;
// Generated May 13, 2020 4:41:31 PM by Hibernate Tools 5.2.12.Final

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "menu", catalog = "Storage_Management")
public class Menu  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3067470544134931182L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@Column(name = "parent_menu_id", nullable = false)
	private int parentMenuId;

	@Column(name = "url", nullable = false, length = 100)
	private String url;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "order_index", nullable = false)
	private int orderIndex;

	@Column(name = "active_flag", nullable = false)
	private int activeFlag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false, length = 19)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date", nullable = false, length = 19)
	private Date updatedDate;
	
	// many-to-one
	@OneToMany(mappedBy = "menu", fetch = FetchType.LAZY,
			cascade = {CascadeType.PERSIST, CascadeType.MERGE, 
					CascadeType.DETACH, CascadeType.REFRESH})
	private List<Auth> auths;
	
	// > @Transient co tac dung danh dau cac prop la cac prop khong co trong table cua DB
	//		ma no la cac prop binh thuong do ta tu them vao trong class Entity de lam 
	//		cac viec khac
	// > Cach tot nhat van la nen tach class Entity tong hop thanh 2 class Entity va class DTO
	//	- Trong do class Entity chi nen chua cac prop de mapped voi cac cot trong table cua DB
	//	- Va class DTO se chua cac prop binh thuong va cac prop ben ngoai do ta tu them cao
	//		(chinh la cac prop su dung @Transient trong class nay ne)
	@Transient
	private List<Menu> childMenus;
	@Transient
	private String idMenu;
}
