(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('StagesFollowDeleteController',StagesFollowDeleteController);

    StagesFollowDeleteController.$inject = ['$uibModalInstance', 'entity', 'StagesFollow'];

    function StagesFollowDeleteController($uibModalInstance, entity, StagesFollow) {
        var vm = this;
        vm.stagesFollow = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            StagesFollow.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
