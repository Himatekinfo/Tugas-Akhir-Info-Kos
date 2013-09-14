<?php
/**
 * Controller is the customized base controller class.
 * All controller classes for this application should extend from this base class.
 */
class Controller extends CController
{
	/**
	 * @var string the default layout for the controller view. Defaults to '//layouts/column1',
	 * meaning using a single column layout. See 'protected/views/layouts/column1.php'.
	 */
	public $layout='//layouts/column1';
	/**
	 * @var array context menu items. This property will be assigned to {@link CMenu::items}.
	 */
	public $menu=array();
	/**
	 * @var array the breadcrumbs of the current page. The value of this property will
	 * be assigned to {@link CBreadcrumbs::links}. Please refer to {@link CBreadcrumbs::links}
	 * for more details on how to specify this property.
	 */
	public $breadcrumbs=array();
        
        protected function publicActions() {
            return array();
        }
        
        protected function beforeAction($action) {
		if(isset($_POST['sessionToken']))
		{
			$model = UserSession::prolong($_POST['sessionToken']);
			if($model !== false)
			{
				$this->userModel = $model;
				return true;
			}
		}
                
                if(in_array($action->id, $this->publicActions()))
                {
                   return true; 
                }

		Helper::returnData(array("message"=>"Session Token not found or has been expired"), 403);
		return false;
	}
        
        public function actionIndex()
	{
		echo "Available methods: <br />";
		echo "<hr />";
		Helper::printMethods(__CLASS__);
	}
}