(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('StagesDeleteController',StagesDeleteController);

    StagesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Stages'];

    function StagesDeleteController($uibModalInstance, entity, Stages) {
        var vm = this;
        vm.stages = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Stages.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
