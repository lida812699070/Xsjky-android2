package cn.xsjky.android.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Unique;

/**
 * Created by OK on 2016/3/26.
 */
@Table(name="district")
public class Adress {
    @Id
    private int id;

    @Column(column="name")
    @Unique
    private String name;

    @Column(column="code")
    @Unique
    private String code;

}
