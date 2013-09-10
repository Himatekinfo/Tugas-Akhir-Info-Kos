<?php
/* @var $this PoliceController */
/* @var $dataProvider CActiveDataProvider */

$this->breadcrumbs=array(
	'Polices',
);

$this->menu=array(
	array('label'=>'Create Police', 'url'=>array('create')),
	array('label'=>'Manage Police', 'url'=>array('admin')),
);
?>

<h1>Polices</h1>

<?php $this->widget('zii.widgets.CListView', array(
	'dataProvider'=>$dataProvider,
	'itemView'=>'_view',
)); ?>
