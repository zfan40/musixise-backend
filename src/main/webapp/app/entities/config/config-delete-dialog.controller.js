(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('ConfigDeleteController',ConfigDeleteController);

    ConfigDeleteController.$inject = ['$uibModalInstance', 'entity', 'Config'];

    function ConfigDeleteController($uibModalInstance, entity, Config) {
        var vm = this;
        vm.config = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Config.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
