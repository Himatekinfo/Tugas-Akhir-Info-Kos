<?php
/* @var $this IncidentImageController */
/* @var $model IncidentImage */

$this->breadcrumbs=array(
	'Incident Images'=>array('index'),
	$model->Id=>array('view','id'=>$model->Id),
	'Update',
);

$this->menu=array(
	array('label'=>'List IncidentImage', 'url'=>array('index')),
	array('label'=>'Create IncidentImage', 'url'=>array('create')),
	array('label'=>'View IncidentImage', 'url'=>array('view', 'id'=>$model->Id)),
	array('label'=>'Manage IncidentImage', 'url'=>array('admin')),
);
?>

<h1>Update IncidentImage <?php echo $model->Id; ?></h1>

<?php $this->renderPartial('_form', array('model'=>$model)); ?>