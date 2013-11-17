<?php

/**
 * This is the model class for table "tbl_rumahsewa".
 *
 * The followings are the available columns in table 'tbl_rumahsewa':
 * @property string $id_rumahSewa
 * @property double $latitude
 * @property double $longitude
 * @property string $namapemilik
 * @property string $alamat
 * @property string $no_telp
 * @property string $hargasewa
 * @property string $foto
 * @property string $fasilitas
 * @property string $deskripsi
 */
class Rumahsewa extends CActiveRecord {

    /**
     * @return string the associated database table name
     */
    public function tableName() {
        return 'tbl_rumahsewa';
    }

    /**
     * @return array validation rules for model attributes.
     */
    public function rules() {
        // NOTE: you should only define rules for those attributes that
        // will receive user inputs.
        return array(
            array('id_rumahSewa, alamat, hargasewa, foto, fasilitas, deskripsi, created_date', 'required'),
            array('latitude, longitude', 'numerical'),
            array('id_rumahSewa, namapemilik', 'length', 'max' => 50),
            array('alamat, foto, fasilitas, deskripsi', 'length', 'max' => 500),
            array('no_telp', 'length', 'max' => 15),
            array('hargasewa', 'length', 'max' => 20),
            // The following rule is used by search().
            // @todo Please remove those attributes that should not be searched.
            array('id_rumahSewa, latitude, longitude, namapemilik, alamat, no_telp, hargasewa, foto, fasilitas, deskripsi', 'safe', 'on' => 'search'),
        );
    }

    /**
     * @return array relational rules.
     */
    public function relations() {
        // NOTE: you may need to adjust the relation name and the related
        // class name for the relations automatically generated below.
        return array(
        );
    }

    /**
     * @return array customized attribute labels (name=>label)
     */
    public function attributeLabels() {
        return array(
            'id_rumahSewa' => 'Id Rumah Sewa',
            'latitude' => 'Latitude',
            'longitude' => 'Longitude',
            'namapemilik' => 'Namapemilik',
            'alamat' => 'Alamat',
            'no_telp' => 'No Telp',
            'hargasewa' => 'Hargasewa',
            'foto' => 'Foto',
            'fasilitas' => 'Fasilitas',
            'deskripsi' => 'Deskripsi',
        );
    }

    /**
     * Retrieves a list of models based on the current search/filter conditions.
     *
     * Typical usecase:
     * - Initialize the model fields with values from filter form.
     * - Execute this method to get CActiveDataProvider instance which will filter
     * models according to data in model fields.
     * - Pass data provider to CGridView, CListView or any similar widget.
     *
     * @return CActiveDataProvider the data provider that can return the models
     * based on the search/filter conditions.
     */
    public function search() {
        // @todo Please modify the following code to remove attributes that should not be searched.

        $criteria = new CDbCriteria;

        $criteria->compare('id_rumahSewa', $this->id_rumahSewa, true);
        $criteria->compare('latitude', $this->latitude);
        $criteria->compare('longitude', $this->longitude);
        $criteria->compare('namapemilik', $this->namapemilik, true);
        $criteria->compare('alamat', $this->alamat, true);
        $criteria->compare('no_telp', $this->no_telp, true);
        $criteria->compare('hargasewa', $this->hargasewa, true);
        $criteria->compare('foto', $this->foto, true);
        $criteria->compare('fasilitas', $this->fasilitas, true);
        $criteria->compare('deskripsi', $this->deskripsi, true);

        return new CActiveDataProvider($this, array(
            'criteria' => $criteria,
        ));
    }

    /**
     * Returns the static model of the specified AR class.
     * Please note that you should have this exact method in all your CActiveRecord descendants!
     * @param string $className active record class name.
     * @return Rumahsewa the static model class
     */
    public static function model($className = __CLASS__) {
        return parent::model($className);
    }

}
